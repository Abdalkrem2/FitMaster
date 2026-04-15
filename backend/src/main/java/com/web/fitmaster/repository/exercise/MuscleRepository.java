package com.web.fitmaster.repository.exercise;

import com.web.fitmaster.model.exercise.Muscle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuscleRepository extends JpaRepository<Muscle, Long> {
}
