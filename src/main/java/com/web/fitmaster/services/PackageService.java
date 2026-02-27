package com.web.fitmaster.services;

import com.web.fitmaster.model.Package;
import java.util.List;

public interface PackageService {

    Package createPackage(Package pkg);

    Package updatePackage(Long id, Package pkg);

    void deletePackage(Long id);

    List<Package> getAllPackages();

    Package getPackageById(Long id);

    Package changeStatus(Long id);
}