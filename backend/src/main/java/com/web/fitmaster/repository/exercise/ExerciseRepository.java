package com.web.fitmaster.repository.exercise;

import com.web.fitmaster.model.enums.DifficultyLevel;
import com.web.fitmaster.model.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, byte[]> {

    // Query 1 — الرئيسية
    @Query("""
    SELECT DISTINCT e FROM Exercise e
    JOIN FETCH e.exerciseMuscles em
    WHERE em.muscle.id IN :muscleIds
    AND em.role = com.web.fitmaster.model.enums.MuscleRole.PRIMARY
    AND e.difficultyLevel = :difficulty
    AND e.isArchived = false
""")
    List<Exercise> findByPrimaryMusclesAndDifficulty(
            @Param("muscleIds") List<Long> muscleIds,
            @Param("difficulty") DifficultyLevel difficulty
    );

    // Query 2 — تجيب الـ translations
    @Query("""
    SELECT DISTINCT e FROM Exercise e
    LEFT JOIN FETCH e.translations
    WHERE e IN :exercises
""")
    List<Exercise> fetchTranslations(@Param("exercises") List<Exercise> exercises);

    // Query 3 — تجيب الـ media
    @Query("""
    SELECT DISTINCT e FROM Exercise e
    LEFT JOIN FETCH e.media med
    LEFT JOIN FETCH med.mediaAsset
    WHERE e IN :exercises
""")
    List<Exercise> fetchMedia(@Param("exercises") List<Exercise> exercises);




}
