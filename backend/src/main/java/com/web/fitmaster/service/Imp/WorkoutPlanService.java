package com.web.fitmaster.service.Imp;

import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.*;
import com.web.fitmaster.model.enums.DifficultyLevel;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import com.web.fitmaster.model.exercise.Exercise;
import com.web.fitmaster.repository.MemberProfileRepository;
import com.web.fitmaster.repository.WorkoutPlanRepository;
import com.web.fitmaster.workout.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final MemberProfileRepository memberProfileRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final TrainingConfigFactory configFactory;
    private final ExerciseSelector exerciseSelector;

    public List<WorkoutPlan> getAllPlans(Long memberId) {
        return workoutPlanRepository
                .findByMember_IdOrderByCreatedAtDesc(memberId);
    }


    public WorkoutPlan getActivePlan(Long memberId) {
        return workoutPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("No active workout plan found"));
    }

    @Transactional
    public WorkoutPlan generatePlan(Long memberId) {


        MemberProfile profile = memberProfileRepository.findByMemberIdAndMember_IsActivatedTrueAndMember_DeletedFalse(memberId)
                    .orElseThrow(() -> new RuntimeException("Member profile not found"));


        workoutPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .ifPresent(old -> {
                    old.setStatus(WorkoutPlanStatus.ARCHIVED);
                    workoutPlanRepository.save(old);
                });


        TrainingConfig config = configFactory.create(profile);


        DifficultyLevel difficulty = mapDifficulty(profile.getFitnessLevel());//يمكن بدها تعديل

        // 5. ابني الـ WorkoutPlan
        WorkoutPlan plan = WorkoutPlan.builder()
                .member(profile.getMember())
                .name(buildPlanName(profile))
                .splitType(profile.getSplitType())
                .goal(profile.getGoal())
                .level(profile.getFitnessLevel())
                .status(WorkoutPlanStatus.ACTIVE)
                .workoutDays(new ArrayList<>())
                .build();

        // 6. لكل DaySplit — ابني WorkoutDay
        Set<String> excludedIds = new HashSet<>();
        int dayNumber = 1;

        for (DaySplit daySplit : config.daySplits()) {
            List<Exercise> exercises = exerciseSelector
                    .selectForDay(daySplit, difficulty, excludedIds);

            WorkoutDay day = WorkoutDay.builder()
                    .workoutPlan(plan)
                    .dayNumber(dayNumber++)
                    .muscleGroupLabel(daySplit.label())
                    .workoutExercises(new ArrayList<>())
                    .build();

            // 7. لكل Exercise — ابني WorkoutExercise
            int order = 1;
            for (Exercise exercise : exercises) {
                WorkoutExercise we = WorkoutExercise.builder()
                        .workoutDay(day)
                        .exercise(exercise)
                        .orderIndex(order++)
                        .sets(config.sets())
                        .reps(config.repsMin())//fixed
                        .repsMax(config.repsMax())
                        .build();

                day.getWorkoutExercises().add(we);
            }

            plan.getWorkoutDays().add(day);
        }

        // 8. احفظ الخطة كاملة
        return workoutPlanRepository.save(plan);
    }

    // FitnessLevel → DifficultyLevel
    private DifficultyLevel mapDifficulty(FitnessLevel level) {
        return switch (level) {
            case BEGINNER     -> DifficultyLevel.BEGINNER;
            case INTERMEDIATE -> DifficultyLevel.INTERMEDIATE;
            case ADVANCED     -> DifficultyLevel.ADVANCED;
        };
    }

    private String buildPlanName(MemberProfile profile) {
        return profile.getSplitType().name().replace("_", " ")
                + " - " + profile.getGoal().name().replace("_", " ");
    }
}