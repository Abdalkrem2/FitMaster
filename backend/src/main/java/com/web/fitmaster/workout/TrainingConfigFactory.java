package com.web.fitmaster.workout;

import com.web.fitmaster.model.MemberProfile;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.SplitType;
import com.web.fitmaster.model.enums.TrainingStyle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingConfigFactory {

    public TrainingConfig create(MemberProfile profile) {
        int sets = resolveSets(profile.getFitnessLevel(), profile.getTrainingStyle());
        int[] reps = resolveReps(profile.getTrainingStyle());
        List<DaySplit> splits = resolveSplits(profile.getSplitType());

        return new TrainingConfig(sets, reps[0], reps[1], splits);
    }



    private int resolveSets(FitnessLevel level, TrainingStyle style) { // V1
        if (style == TrainingStyle.CIRCUIT) return 3;
        return switch (level) {
            case BEGINNER     -> 3;
            case INTERMEDIATE -> 4;
            case ADVANCED     -> 5;
        };
    }


    private int[] resolveReps(TrainingStyle style) { //V1 i think i will edit the logic in the near feature
        return switch (style) {
            case STRENGTH    -> new int[]{4, 6};
            case HYPERTROPHY -> new int[]{8, 12};
            case CIRCUIT     -> new int[]{15, 20};
        };
    }



    private List<DaySplit> resolveSplits(SplitType splitType) {
        return switch (splitType) {

            case FULL_BODY -> List.of(
                    new DaySplit("Full Body", fullBodyMuscles()),
                    new DaySplit("Full Body", fullBodyMuscles()),
                    new DaySplit("Full Body", fullBodyMuscles())
            );

            case UPPER_LOWER -> List.of(
                    new DaySplit("Upper Body", upperMuscles()),
                    new DaySplit("Lower Body", lowerMuscles()),
                    new DaySplit("Upper Body", upperMuscles()),
                    new DaySplit("Lower Body", lowerMuscles())
            );

            case BRO_SPLIT_4DAY -> List.of(
                    new DaySplit("Chest & Triceps", List.of(
                            new MuscleTarget("chest",    4),
                            new MuscleTarget("triceps",  3)
                    )),
                    new DaySplit("Back & Biceps", List.of(
                            new MuscleTarget("back",     4),
                            new MuscleTarget("biceps",   3)
                    )),
                    new DaySplit("Shoulders", List.of(
                            new MuscleTarget("shoulders", 5)
                    )),
                    new DaySplit("Legs", List.of(
                            new MuscleTarget("quadriceps", 2),
                            new MuscleTarget("hamstrings", 2),
                            new MuscleTarget("glutes",     1),
                            new MuscleTarget("calves",     1)
                    ))
            );

            case BRO_SPLIT_5DAY -> List.of(
                    new DaySplit("Chest", List.of(
                            new MuscleTarget("chest",    5)
                    )),
                    new DaySplit("Back", List.of(
                            new MuscleTarget("back",     5)
                    )),
                    new DaySplit("Shoulders", List.of(
                            new MuscleTarget("shoulders", 5)
                    )),
                    new DaySplit("Legs", List.of(
                            new MuscleTarget("quadriceps", 2),
                            new MuscleTarget("hamstrings", 2),
                            new MuscleTarget("glutes",     1),
                            new MuscleTarget("calves",     1)
                    )),
                    new DaySplit("Arms", List.of(
                            new MuscleTarget("biceps",   3),
                            new MuscleTarget("triceps",  3)
                    ))
            );

            case PUSH_PULL_LEGS -> List.of(
                    new DaySplit("Push", List.of(
                            new MuscleTarget("chest",     3),
                            new MuscleTarget("shoulders", 2),
                            new MuscleTarget("triceps",   2)
                    )),
                    new DaySplit("Pull", List.of(
                            new MuscleTarget("back",      4),
                            new MuscleTarget("biceps",    2)
                    )),
                    new DaySplit("Legs", List.of(
                            new MuscleTarget("quadriceps", 2),
                            new MuscleTarget("hamstrings", 2),
                            new MuscleTarget("glutes",     1),
                            new MuscleTarget("calves",     1)
                    )),
                    new DaySplit("Push", List.of(
                            new MuscleTarget("chest",     3),
                            new MuscleTarget("shoulders", 2),
                            new MuscleTarget("triceps",   2)
                    )),
                    new DaySplit("Pull", List.of(
                            new MuscleTarget("back",      4),
                            new MuscleTarget("biceps",    2)
                    )),
                    new DaySplit("Legs", List.of(
                            new MuscleTarget("quadriceps", 2),
                            new MuscleTarget("hamstrings", 2),
                            new MuscleTarget("glutes",     1),
                            new MuscleTarget("calves",     1)
                    ))
            );
        };
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private List<MuscleTarget> fullBodyMuscles() {
        return List.of(
                new MuscleTarget("chest",      2),
                new MuscleTarget("back",       2),
                new MuscleTarget("shoulders",  1),
                new MuscleTarget("quadriceps", 2),
                new MuscleTarget("hamstrings", 1)
        );
    }

    private List<MuscleTarget> upperMuscles() {
        return List.of(
                new MuscleTarget("chest",     3),
                new MuscleTarget("back",      3),
                new MuscleTarget("shoulders", 2),
                new MuscleTarget("biceps",    1),
                new MuscleTarget("triceps",   1)
        );
    }

    private List<MuscleTarget> lowerMuscles() {
        return List.of(
                new MuscleTarget("quadriceps", 3),
                new MuscleTarget("hamstrings", 2),
                new MuscleTarget("glutes",     2),
                new MuscleTarget("calves",     1)
        );
    }
}