package com.web.fitmaster.workout;

import com.web.fitmaster.dto.WorkoutPlanDTOs.WorkoutDayResponse;
import com.web.fitmaster.dto.WorkoutPlanDTOs.WorkoutPlanResponse;
import com.web.fitmaster.dto.WorkoutPlanDTOs.WorkoutExerciseResponse;
import com.web.fitmaster.model.WorkoutDay;
import com.web.fitmaster.model.WorkoutExercise;
import com.web.fitmaster.model.WorkoutPlan;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Base64;

@Component
public class WorkoutPlanMapper {

    public WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        return WorkoutPlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .splitType(plan.getSplitType())
                .goal(plan.getGoal())
                .level(plan.getLevel())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .days(plan.getWorkoutDays().stream()
                        .map(this::toDayResponse)
                        .toList())
                .build();
    }

    private WorkoutDayResponse toDayResponse(WorkoutDay day) {
        return WorkoutDayResponse.builder()
                .dayNumber(day.getDayNumber())
                .muscleGroupLabel(day.getMuscleGroupLabel())
                .exercises(day.getWorkoutExercises().stream()
                        .map(this::toExerciseResponse)
                        .toList())
                .build();
    }
    private String toImageUrl(String dbUrl) {
        String code = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
        return "https://res.cloudinary.com/dakuwdt6l/image/upload/exercises/" + code + ".gif";
    }

    private WorkoutExerciseResponse toExerciseResponse(WorkoutExercise we) {
        String instructions = we.getExercise().getTranslations().stream()
                .findFirst()
                .map(t -> t.getInstructions())
                .orElse("");

        String imageUrl = we.getExercise().getMedia().stream()
                .map(em -> toImageUrl(em.getMediaAsset().getUrl()))
                .findFirst()
                .orElse(null);


        String exerciseName = we.getExercise().getTranslations().stream()
                .findFirst()
                .map(t -> t.getName())
                .orElse("Unknown");

        return WorkoutExerciseResponse.builder()
                .id(we.getId())
                .exerciseName(exerciseName)
                .imageUrl(imageUrl)

                .sets(we.getSets())
                .reps(we.getReps())
                .repsMax(we.getRepsMax())
                .instructions(instructions)
                .orderIndex(we.getOrderIndex())
                .build();
    }
}