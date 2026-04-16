package com.web.fitmaster.model.exercise;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

//@Embeddable//هاي معناها انه هاذ الكلاس بنحط داخل كلاس ثاني ك ID
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseMuscleId implements Serializable {

    private byte[] exerciseId;
    private Long muscleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseMuscleId that = (ExerciseMuscleId) o;
        return Arrays.equals(exerciseId, that.exerciseId) && Objects.equals(muscleId, that.muscleId);
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(exerciseId) + Objects.hashCode(muscleId);
    }
}
