package com.web.fitmaster.service.Imp;

import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.*;
import com.web.fitmaster.model.enums.DifficultyLevel;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.WorkoutPlanStatus;
import com.web.fitmaster.model.exercise.Exercise;
import com.web.fitmaster.repository.MemberProfileRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.repository.WorkoutPlanRepository;
import com.web.fitmaster.workout.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final MemberProfileRepository memberProfileRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final TrainingConfigFactory configFactory;
    private final ExerciseSelector exerciseSelector;

    public WorkoutPlan getActivePlan(Long memberId) {
        WorkoutPlan plan = workoutPlanRepository
                .findByMember_IdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("No active workout plan found"));

        // Check if plan has expired (lazy evaluation)
        User member = plan.getMember();
        if (isPlanExpired(plan)) {
            if (plan.getCompletedDaysCount() < plan.getTotalDaysInPlan()) {
                // Streak broken - reset to 0
                member.setDailyStreak(0);
                userRepository.save(member);
            }
            // Mark plan as ended so frontend can show renewal modal
            plan.setIsWeekEnded(true);
        }

        return plan;
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
                .planStartDate(LocalDateTime.now())
                .completedDaysCount(0)
                .build();

        // 6. لكل DaySplit — ابني WorkoutDay
        Set<String> excludedIds = new HashSet<>();
        int dayNumber = 1;
        List<WorkoutDay> days = new ArrayList<>();

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
            days.add(day);
        }

        // Set total days in plan
        plan.setTotalDaysInPlan(dayNumber - 1);
        plan.setPlanEndDate(calculateNextSaturdayEnd());

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

    // ─── New methods for Daily Streak & Weekly Plan Renewal ───────────────────

    @Transactional
    public void markDayCompleted(Long planId, Integer dayNumber) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found"));

        // Increment completed days count
        plan.setCompletedDaysCount(plan.getCompletedDaysCount() + 1);

        // Increment streak on user if first completion of the day
        User member = plan.getMember();
        if (member.getStreakLastUpdated() == null || 
            isNewDay(member.getStreakLastUpdated())) {
            member.setDailyStreak(member.getDailyStreak() + 1);
            member.setStreakLastUpdated(LocalDateTime.now());
            userRepository.save(member);
        }

        workoutPlanRepository.save(plan);
    }

    @Transactional
    public WorkoutPlan renewPlan(Long planId, String choice, String newSplit) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new NotFoundException("Plan not found"));

        Long memberId = plan.getMember().getId();
        MemberProfile profile = memberProfileRepository
                .findByMemberIdAndMember_IsActivatedTrueAndMember_DeletedFalse(memberId)
                .orElseThrow(() -> new NotFoundException("Member profile not found"));

        if ("NEW_SPLIT".equals(choice) && newSplit != null) {
            try {
                profile.setSplitType(com.web.fitmaster.model.enums.SplitType.valueOf(newSplit));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid split type: " + newSplit);
            }
            memberProfileRepository.save(profile);
            plan.setSplitType(profile.getSplitType());
        }

        if ("SAME_PLAN".equals(choice)) {
            // Keep exercises, just reset dates and counts
            plan.setPlanStartDate(LocalDateTime.now());
            plan.setPlanEndDate(calculateNextSaturdayEnd());
            plan.setCompletedDaysCount(0);
            plan.setIsWeekEnded(false);
            return workoutPlanRepository.save(plan);
        }

        // For NEW_SPLIT or SAME_SPLIT_NEW_EXERCISES: clear old days and generate new ones
        plan.getWorkoutDays().clear();
        
        TrainingConfig config = configFactory.create(profile);
        DifficultyLevel difficulty = mapDifficulty(profile.getFitnessLevel());
        plan.setName(buildPlanName(profile));
        
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

            int order = 1;
            for (Exercise exercise : exercises) {
                WorkoutExercise we = WorkoutExercise.builder()
                        .workoutDay(day)
                        .exercise(exercise)
                        .orderIndex(order++)
                        .sets(config.sets())
                        .reps(config.repsMin())
                        .repsMax(config.repsMax())
                        .build();

                day.getWorkoutExercises().add(we);
            }
            plan.getWorkoutDays().add(day);
        }

        plan.setTotalDaysInPlan(dayNumber - 1);
        plan.setPlanStartDate(LocalDateTime.now());
        plan.setPlanEndDate(calculateNextSaturdayEnd());
        plan.setCompletedDaysCount(0);
        plan.setIsWeekEnded(false);

        return workoutPlanRepository.save(plan);
    }

    public boolean isPlanExpired(WorkoutPlan plan) {
        if (plan.getPlanEndDate() == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(plan.getPlanEndDate());
    }

    private LocalDateTime calculateNextSaturdayEnd() {
        LocalDateTime now = LocalDateTime.now();
        // Find next Saturday (day of week 6)
        int daysUntilSaturday = (6 - now.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilSaturday == 0) {
            daysUntilSaturday = 7; // If today is Saturday, go to next Saturday
        }
        return now.plusDays(daysUntilSaturday).withHour(23).withMinute(59).withSecond(0);
    }

    private boolean isNewDay(LocalDateTime lastUpdated) {
        LocalDateTime now = LocalDateTime.now();
        return !now.toLocalDate().isEqual(lastUpdated.toLocalDate());
    }
}