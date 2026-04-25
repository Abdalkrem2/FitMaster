package com.web.fitmaster.service.Imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.fitmaster.model.*;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import com.web.fitmaster.repository.MemberProfileRepository;
import com.web.fitmaster.repository.NutritionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NutritionPlanService {

    private final MemberProfileRepository memberProfileRepository;
    private final NutritionPlanRepository nutritionPlanRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public NutritionPlan getActivePlan(Long memberId) {
        return nutritionPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active nutrition plan found"));
    }
    @Transactional
    public NutritionPlan generatePlan(Long memberId) {

        // 1. جيب الـ MemberProfile
        MemberProfile profile = memberProfileRepository
                .findByMemberIdAndMember_IsActivatedTrueAndMember_DeletedFalse(memberId)
                .orElseThrow(() -> new RuntimeException("Member profile not found"));

        User member = profile.getMember();

        // 2. احسب السعرات والماكرو
        int calories = calculateTargetCalories(profile, member);
        int protein  = (int) (profile.getWeight() * 2);
        int fat      = (int) ((calories * 0.25) / 9);
        int carbs    = (int) ((calories - (protein * 4) - (fat * 9)) / 4);

        // 3. Archive الخطة القديمة
        nutritionPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .ifPresent(old -> {
                    old.setStatus(WorkoutPlanStatus.ARCHIVED);
                    nutritionPlanRepository.save(old);
                });

        // 4. بني الـ Prompt
        String prompt = buildPrompt(profile, member, calories, protein, carbs, fat);

        // 5. استدعي Gemini مع Retry
        String jsonResponse = callGeminiWithRetry(prompt, 3);

        // 6. Parse الـ Response
        NutritionPlan plan = parseAndBuildPlan(jsonResponse, profile, member, calories, protein, carbs, fat);

        // 7. احفظ
        return nutritionPlanRepository.save(plan);
    }

    // ─── Calories Calculation ────────────────────────────────────────────────

    private int calculateTargetCalories(MemberProfile profile, User member) {
        double weight = profile.getWeight();
        double height = profile.getHeight();
        int age = profile.getAge();

        // Mifflin-St Jeor
        double bmr;
        if ("MALE".equalsIgnoreCase(member.getGender())) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        // Activity Factor
        double tdee = switch (profile.getFitnessLevel()) {
            case BEGINNER     -> bmr * 1.375;
            case INTERMEDIATE -> bmr * 1.55;
            case ADVANCED     -> bmr * 1.725;
        };

        // Target based on goal
        return (int) switch (profile.getGoal()) {
            case MUSCLE_GAIN     -> tdee + 300;
            case WEIGHT_LOSS     -> tdee - 500;
            case ENDURANCE       -> tdee + 200;
            case GENERAL_FITNESS -> tdee;
        };
    }

    // ─── Prompt Builder ──────────────────────────────────────────────────────

    private String buildPrompt(MemberProfile profile, User member,
                               int calories, int protein, int carbs, int fat) {
        StringBuilder sb = new StringBuilder();
        sb.append("Generate a detailed daily meal plan in JSON format.\n\n");
        sb.append("Member info:\n");
        sb.append("- Goal: ").append(profile.getGoal()).append("\n");
        sb.append("- Age: ").append(profile.getAge()).append("\n");
        sb.append("- Weight: ").append(profile.getWeight()).append("kg\n");
        sb.append("- Height: ").append(profile.getHeight()).append("cm\n");
        sb.append("- Gender: ").append(member.getGender()).append("\n");

        // Health conditions
        if (profile.isHasDiabetes()) sb.append("- Has diabetes: avoid simple sugars, low glycemic foods only\n");
        if (profile.isHasHeartConditions()) sb.append("- Has heart condition: avoid saturated fats, low sodium\n");
        if (profile.isHasHypertension()) sb.append("- Has hypertension: very low sodium\n");

        // Allergies
        if (profile.getAllergies() != null && !profile.getAllergies().isEmpty()) {
            sb.append("- Allergies: ").append(profile.getAllergies()).append(" — STRICTLY avoid these\n");
        }

        sb.append("\nNutrition targets:\n");
        sb.append("- Daily calories: ").append(calories).append(" kcal\n");
        sb.append("- Protein: ").append(protein).append("g\n");
        sb.append("- Carbs: ").append(carbs).append("g\n");
        sb.append("- Fat: ").append(fat).append("g\n");

        sb.append("""
            
            STRICT RULES:
            1. Return ONLY valid JSON — no markdown, no explanation, no extra text
            2. Include exactly 5 meals: Breakfast, Morning Snack, Lunch, Afternoon Snack, Dinner
            3. Total calories must be within ±50 of the target
            4. Each meal must have at least 2 foods
            5. Include realistic prep time for each meal
            6. Include step-by-step recipe instructions for each meal
            
            Return this exact JSON structure:
            {
              "meals": [
                {
                  "name": "Breakfast",
                  "mealTime": "8:00 AM",
                  "prepTime": "10 minutes",
                  "totalCalories": 600,
                  "foods": [
                    {
                      "name": "Oatmeal",
                      "amount": "100g",
                      "calories": 350,
                      "proteinGrams": 12,
                      "carbsGrams": 60,
                      "fatGrams": 7
                    }
                  ],
                  "recipeSteps": [
                    {"stepOrder": 1, "instruction": "Boil water or milk"},
                    {"stepOrder": 2, "instruction": "Add oats and cook for 5 minutes"}
                  ]
                }
              ]
            }
            """);

        return sb.toString();
    }

    // ─── Gemini API Call ─────────────────────────────────────────────────────

    private String callGeminiWithRetry(String prompt, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String response = callGemini(prompt);
                String clean = cleanResponse(response);
                validateJson(clean);
                return clean;
            } catch (Exception e) {
                if (attempt == maxRetries) {
                    throw new RuntimeException("Gemini failed after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        throw new RuntimeException("Gemini failed");
    }

    private String callGemini(String prompt) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // ابني الـ request body بشكل صح
        String requestBody = mapper.writeValueAsString(
                Map.of("contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                ))
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geminiApiUrl + "?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: " + response.statusCode()
                    + " — " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        return root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();
    }

    private String cleanResponse(String response) {
        return response
                .replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();
    }

    private void validateJson(String json) throws Exception {
        JsonNode root = new ObjectMapper().readTree(json);
        JsonNode meals = root.path("meals");
        if (meals.isMissingNode() || meals.size() < 3) {
            throw new RuntimeException("Invalid meal plan structure");
        }
    }

    // ─── Parse & Build ───────────────────────────────────────────────────────

    private NutritionPlan parseAndBuildPlan(String json, MemberProfile profile, User member,
                                            int calories, int protein, int carbs, int fat) {
        try {
            JsonNode root = new ObjectMapper().readTree(json);
            JsonNode mealsNode = root.path("meals");

            NutritionPlan plan = NutritionPlan.builder()
                    .member(member)
                    .goal(profile.getGoal())
                    .dailyCalories(calories)
                    .proteinGrams(protein)
                    .carbsGrams(carbs)
                    .fatGrams(fat)
                    .status(WorkoutPlanStatus.ACTIVE)
                    .meals(new ArrayList<>())
                    .build();

            for (JsonNode mealNode : mealsNode) {
                NutritionMeal meal = NutritionMeal.builder()
                        .nutritionPlan(plan)
                        .name(mealNode.path("name").asText())
                        .mealTime(mealNode.path("mealTime").asText())
                        .prepTime(mealNode.path("prepTime").asText())
                        .totalCalories(mealNode.path("totalCalories").asInt())
                        .foods(new ArrayList<>())
                        .recipeSteps(new ArrayList<>())
                        .build();

                // Foods
                for (JsonNode foodNode : mealNode.path("foods")) {
                    NutritionFood food = NutritionFood.builder()
                            .meal(meal)
                            .name(foodNode.path("name").asText())
                            .amount(foodNode.path("amount").asText())
                            .calories(foodNode.path("calories").asInt())
                            .proteinGrams(foodNode.path("proteinGrams").asInt())
                            .carbsGrams(foodNode.path("carbsGrams").asInt())
                            .fatGrams(foodNode.path("fatGrams").asInt())
                            .build();
                    meal.getFoods().add(food);
                }

                // Recipe Steps
                for (JsonNode stepNode : mealNode.path("recipeSteps")) {
                    NutritionRecipeStep step = NutritionRecipeStep.builder()
                            .meal(meal)
                            .stepOrder(stepNode.path("stepOrder").asInt())
                            .instruction(stepNode.path("instruction").asText())
                            .build();
                    meal.getRecipeSteps().add(step);
                }

                plan.getMeals().add(meal);
            }

            return plan;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}