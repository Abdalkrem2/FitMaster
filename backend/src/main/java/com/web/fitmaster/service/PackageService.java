package com.web.fitmaster.service;

import com.web.fitmaster.dto.PackageDTOs;
import com.web.fitmaster.model.enums.PackageStatus;
import org.jspecify.annotations.Nullable;


import java.util.List;

public interface PackageService {
     List<PackageDTOs.PackageDTO> getAllPackages();

   String  deletePackage(Long id);

    PackageDTOs. PackageDTO createPackage(PackageDTOs.CreatePackageRequest req);

    PackageDTOs. PackageDTO updatePackage(Long id, PackageDTOs.UpdatePackageRequest req);

    PackageDTOs.@Nullable PackageDTO getPackage(Long id);

    PackageDTOs.@Nullable PackageDTO updateStatus(Long id, PackageDTOs.UpdatePackageStatus status);
}
