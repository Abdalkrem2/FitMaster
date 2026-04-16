package com.web.fitmaster.repository.exercise;

import com.web.fitmaster.model.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, byte[]> {
}
