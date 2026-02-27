package com.web.fitmaster.repository;

import com.web.fitmaster.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
}