package com.web.fitmaster.model.exercise;

import com.web.fitmaster.model.enums.MuscleRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exercise_muscles")
@IdClass(ExerciseMuscleId.class)
@Getter
@Setter
@NoArgsConstructor
public class ExerciseMuscle {
//    @EmbeddedId //composite key يعني
//    private ExerciseMuscleId id;

    @Id
    @Column(name = "exercise_id", columnDefinition = "BINARY(16)")
    private byte[] exerciseId;

    @Id
    @Column(name = "muscle_id")
    private Long muscleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", insertable = false, updatable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muscle_id", insertable = false, updatable = false)
    private Muscle muscle;
//اعلملنا هاذ الكلاس بس مشان نعرف ال role
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MuscleRole role;
}
