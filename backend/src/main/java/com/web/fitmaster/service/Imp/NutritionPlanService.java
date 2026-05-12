package com.web.fitmaster.service.Imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.fitmaster.dto.NutritionPlanDTOs;
import com.web.fitmaster.model.*;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import com.web.fitmaster.nutrition.NutritionPlanMapper;
import com.web.fitmaster.repository.MemberProfileRepository;
import com.web.fitmaster.repository.NutritionPlanRepository;
import com.web.fitmaster.workout.WorkoutPlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NutritionPlanService {

    private final MemberProfileRepository memberProfileRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final NutritionPlanMapper nutritionPlanMapper;
    private final WorkoutPlanMapper workoutPlanMapper;
    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;


@Transactional
    public NutritionPlanDTOs.NutritionPlanResponse getActivePlan(Long memberId) {
       NutritionPlan nutritionPlan= nutritionPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active nutrition plan found"));
       return nutritionPlanMapper.toResponse(nutritionPlan);
    }
@Transactional
    public List<NutritionPlanDTOs.NutritionPlanResponse> getAllPlans(Long memberId) {
       List<NutritionPlan> nutritionPlans= nutritionPlanRepository
                .findByMember_IdOrderByCreatedAtDesc(memberId);
       return nutritionPlans.stream().map(nutritionPlanMapper::toResponse).toList();
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
        String jsonResponse = callgorqWithRetry(prompt, 3);

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

        sb.append("Create a daily meal plan with EXACTLY ").append(calories).append(" kcal total.\n");
        sb.append("Split across 5 meals, each meal ~").append(calories / 5).append(" kcal.\n\n");

        sb.append("Goal: ").append(profile.getGoal()).append("\n");
        sb.append("Targets: ").append(protein).append("g protein, ")
                .append(carbs).append("g carbs, ")
                .append(fat).append("g fat\n");

        if (profile.isHasDiabetes()) sb.append("Diabetic: low glycemic foods only\n");
        if (profile.isHasHeartConditions()) sb.append("Heart condition: low saturated fat\n");
        if (profile.isHasHypertension()) sb.append("Hypertension: low sodium\n");
        if (profile.getAllergies() != null && !profile.getAllergies().isEmpty()) {
            sb.append("AVOID: ").append(profile.getAllergies()).append("\n");
        }

        sb.append("\nRULES:\n");
        sb.append("1. Return ONLY valid JSON, no markdown\n");
        sb.append("2. Exactly 5 meals: Breakfast, Morning Snack, Lunch, Afternoon Snack, Dinner\n");
        sb.append("3. Each meal must reach ~").append(calories / 5).append(" kcal — use large portions\n");
        sb.append("4. At least 2 foods per meal\n");
        sb.append("5. Max 3 recipe steps per meal\n\n");

        sb.append("JSON structure:\n");
        sb.append("{\"meals\":[{\"name\":\"Breakfast\",\"mealTime\":\"8:00 AM\",\"prepTime\":\"10 minutes\",");
        sb.append("\"totalCalories\":").append(calories / 5).append(",");
        sb.append("\"foods\":[{\"name\":\"food\",\"amount\":\"100g\",\"calories\":300,");
        sb.append("\"proteinGrams\":20,\"carbsGrams\":30,\"fatGrams\":10}],");
        sb.append("\"recipeSteps\":[{\"stepOrder\":1,\"instruction\":\"step here\"}]}]}\n");

        return sb.toString();
    }
    // ─── Gemini API Call ─────────────────────────────────────────────────────

    private String callgorqWithRetry(String prompt, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String response = callGroq(prompt);
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

    private String callGroq(String prompt) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String requestBody = mapper.writeValueAsString(
                Map.of(
                        "model", "llama-3.3-70b-versatile",
                        "messages", List.of(
                                Map.of("role", "user", "content", prompt)
                        ),
                        "temperature", 0.7
                )
        );

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(groqApiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + groqApiKey)
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Groq API error: " + response.statusCode()
                    + " — " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        return root.path("choices").get(0)
                .path("message")
                .path("content").asText();
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
                    .dailyCalories(calories) // مؤقت، رح نعدله بعدين
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
                        .totalCalories(0)
                        .foods(new ArrayList<>())
                        .recipeSteps(new ArrayList<>())
                        .build();

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

                int mealCalories = meal.getFoods().stream()
                        .mapToInt(NutritionFood::getCalories)
                        .sum();
                meal.setTotalCalories(mealCalories);

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

            // ← هنا نحسب المجموع الفعلي من الوجبات
            int actualCalories = plan.getMeals().stream()
                    .mapToInt(NutritionMeal::getTotalCalories)
                    .sum();

            int actualProtein = plan.getMeals().stream()
                    .flatMap(meal -> meal.getFoods().stream())
                    .mapToInt(NutritionFood::getProteinGrams)
                    .sum();

            int actualCarbs = plan.getMeals().stream()
                    .flatMap(meal -> meal.getFoods().stream())
                    .mapToInt(NutritionFood::getCarbsGrams)
                    .sum();

            int actualFat = plan.getMeals().stream()
                    .flatMap(meal -> meal.getFoods().stream())
                    .mapToInt(NutritionFood::getFatGrams)
                    .sum();

            plan.setDailyCalories(actualCalories);
            plan.setProteinGrams(actualProtein);
            plan.setCarbsGrams(actualCarbs);
            plan.setFatGrams(actualFat);

            return plan;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}