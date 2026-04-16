package com.web.fitmaster.model.exercise;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseEquipmentId implements Serializable {
    private byte[] exerciseId;
    private Long equipmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseEquipmentId that = (ExerciseEquipmentId) o;
        return Arrays.equals(exerciseId, that.exerciseId) && Objects.equals(equipmentId, that.equipmentId);
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(exerciseId) + Objects.hashCode(equipmentId);
    }
}
