package com.web.fitmaster.repository.exercise;

import com.web.fitmaster.model.exercise.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
