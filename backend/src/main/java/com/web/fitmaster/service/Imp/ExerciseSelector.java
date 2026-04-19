package com.web.fitmaster.service.Imp;

import com.web.fitmaster.model.enums.DifficultyLevel;
import com.web.fitmaster.model.enums.MuscleRole;
import com.web.fitmaster.model.exercise.Exercise;
import com.web.fitmaster.repository.exercise.ExerciseRepository;
import com.web.fitmaster.workout.DaySplit;
import com.web.fitmaster.workout.MuscleGroupMapper;
import com.web.fitmaster.workout.MuscleTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseSelector {
    private final ExerciseRepository exerciseRepository;
    private final MuscleGroupMapper muscleGroupMapper;

    // رح تُستدعى لكل DaySplit
    public List<Exercise> selectForDay(DaySplit daySplit,
                                       DifficultyLevel difficulty,
                                       Set<String> excludedIds) {
        List<Exercise> dayExercises = new ArrayList<>();

        for (MuscleTarget target : daySplit.muscles()) {
            List<Long> muscleIds = muscleGroupMapper.getMuscleIds(target.muscle());

            // جيب المرشحين من الـ DB
            // جيب المرشحين
            List<Exercise> candidates = exerciseRepository
                    .findByPrimaryMusclesAndDifficulty(muscleIds, difficulty);

            // Fallback: If no exercises match the specific difficulty for this muscle, get any difficulty
            if (candidates.isEmpty()) {
                candidates = exerciseRepository.findByPrimaryMuscles(muscleIds);
            }

// جيب الـ translations والـ media بشكل منفصل
            if (!candidates.isEmpty()) {
                exerciseRepository.fetchTranslations(candidates);
                exerciseRepository.fetchMedia(candidates);
            }

            // استبعد المكررة — نحول byte[] لـ String عشان المقارنة تشتغل
            candidates.removeIf(e -> excludedIds.contains(toHex(e.getId())));

            // صنّف compound و isolation
            List<Exercise> compounds = candidates.stream()
                    .filter(e -> e.getExerciseMuscles().stream()
                            .anyMatch(em -> em.getRole() == MuscleRole.SECONDARY))
                    .collect(Collectors.toList());

            List<Exercise> isolations = candidates.stream()
                    .filter(e -> e.getExerciseMuscles().stream()
                            .noneMatch(em -> em.getRole() == MuscleRole.SECONDARY))
                    .collect(Collectors.toList());

            // اختار التمارين
            List<Exercise> selected = pickExercises(compounds, isolations, target.exerciseCount());

            // أضف للـ excludedIds
            selected.forEach(e -> excludedIds.add(toHex(e.getId())));

            dayExercises.addAll(selected);
        }

        return dayExercises;
    }

    private List<Exercise> pickExercises(List<Exercise> compounds,
                                         List<Exercise> isolations,
                                         int count) {
        Collections.shuffle(compounds);
        Collections.shuffle(isolations);

        // ثلثين compound، ثلث isolation
        int compoundCount = (int) Math.ceil(count * 0.66);
        int isolationCount = count - compoundCount;

        List<Exercise> selected = new ArrayList<>();
        selected.addAll(compounds.subList(0, Math.min(compoundCount, compounds.size())));
        selected.addAll(isolations.subList(0, Math.min(isolationCount, isolations.size())));

        // لو ما كفى، كمّل من المتبقي
        if (selected.size() < count) {
            List<Exercise> remaining = new ArrayList<>(compounds);
            remaining.addAll(isolations);
            remaining.removeAll(selected);
            Collections.shuffle(remaining);
            int needed = count - selected.size();
            selected.addAll(remaining.subList(0, Math.min(needed, remaining.size())));
        }

        return selected;
    }

    // نحول byte[] لـ hex String عشان المقارنة في Set تشتغل صح
    private String toHex(byte[] id) {
        StringBuilder sb = new StringBuilder();
        for (byte b : id) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

