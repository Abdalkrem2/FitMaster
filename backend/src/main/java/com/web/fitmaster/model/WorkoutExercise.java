package com.web.fitmaster.model;

import com.web.fitmaster.model.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "workout_exercises")
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_day_id", nullable = false)
    private WorkoutDay workoutDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", columnDefinition = "BINARY(16)", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;   // ترتيب التمرين في اليوم

    private Integer sets;

    private Integer reps;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;  // بديل لـ reps في تمارين الـ cardio

}
