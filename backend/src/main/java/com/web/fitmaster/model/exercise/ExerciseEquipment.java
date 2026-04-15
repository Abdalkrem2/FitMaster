package com.web.fitmaster.model.exercise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exercise_equipment")
@IdClass(ExerciseEquipmentId.class)
@Getter
@Setter
@NoArgsConstructor
public class ExerciseEquipment {
//    @EmbeddedId
//    private ExerciseEquipmentId id;

    @Id
    @Column(name = "exercise_id", columnDefinition = "VARBINARY(16)")
    private byte[] exerciseId;

    @Id
    @Column(name = "equipment_id")
    private Long equipmentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("equipmentId")
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;


    @Column(name = "is_required")
    private boolean isRequired;
}
