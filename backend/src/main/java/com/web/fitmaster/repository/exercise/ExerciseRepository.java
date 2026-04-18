package com.web.fitmaster.repository.exercise;

import com.web.fitmaster.model.enums.DifficultyLevel;
import com.web.fitmaster.model.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, byte[]> {

    @Query("""
    SELECT DISTINCT e FROM Exercise e
    JOIN FETCH e.exerciseMuscles em
    WHERE em.muscle.id IN :muscleIds
    AND em.role = MuscleRole.PRIMARY
    AND e.difficultyLevel = :difficulty
    AND e.isArchived = false
""")
    List<Exercise> findByPrimaryMusclesAndDifficulty(
            @Param("muscleIds") List<Long> muscleIds,
            @Param("difficulty") DifficultyLevel difficulty
    );
}
