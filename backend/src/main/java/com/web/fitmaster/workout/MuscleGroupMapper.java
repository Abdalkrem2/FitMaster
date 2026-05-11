package com.web.fitmaster.workout;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MuscleGroupMapper {
    private static final Map<String, List<Long>> MUSCLE_MAP= Map.ofEntries(
            Map.entry("chest",       List.of(15L, 8L, 31L)),
            Map.entry("back",        List.of(28L, 5L, 32L, 7L, 18L)),
            Map.entry("shoulders",   List.of(10L, 21L, 20L, 26L, 24L, 23L)),
            Map.entry("biceps",      List.of(6L, 25L)),
            Map.entry("triceps",     List.of(9L)),
            Map.entry("quadriceps",  List.of(11L, 16L)),
            Map.entry("hamstrings",  List.of(13L)),
            Map.entry("glutes",      List.of(14L)),
            Map.entry("calves",      List.of(17L, 41L)),
            Map.entry("abs",         List.of(1L, 48L, 4L, 3L, 37L))
    );

    public List<Long> getMuscleIds(String muscleGroup) {
        List<Long> ids = MUSCLE_MAP.get(muscleGroup.toLowerCase());
        if (ids == null) {
            throw new IllegalArgumentException("Unknown muscle group: " + muscleGroup);
        }
        return ids;
    }
}
