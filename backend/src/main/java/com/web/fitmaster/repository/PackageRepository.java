package com.web.fitmaster.repository;

import com.web.fitmaster.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
   Optional<Package>findByPackageIdAndDeletedFalse (Long id);

    List< Package > findAllByDeletedFalse();
}
