package com.web.fitmaster.services.Imp;


import com.web.fitmaster.model.Package;
import com.web.fitmaster.model.enums.PackageStatus;
import com.web.fitmaster.repository.PackageRepository;
import com.web.fitmaster.services.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {


    private final PackageRepository packageRepository;


    @Override
    public Package createPackage(Package pkg) {
        pkg.setStatus(PackageStatus.ACTIVE);
        return packageRepository.save(pkg);
    }

    @Override
    public Package updatePackage(Long id, Package pkg) {
        Package existing = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        existing.setName(pkg.getName());
        existing.setDurationInDays(pkg.getDurationInDays());
        existing.setPrice(pkg.getPrice());
        existing.setDescription(pkg.getDescription());

        return packageRepository.save(existing);
    }

    @Override
    public void deletePackage(Long id) {
        Package pkg = getPackageById(id);
        packageRepository.delete(pkg);
    }

    @Override
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public Package getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found: "+ id));
    }

    @Override
    public Package changeStatus(Long id) {
        Package pkg = getPackageById(id);

        if (pkg.getStatus() == PackageStatus.ACTIVE) {
            pkg.setStatus(PackageStatus.INACTIVE);
        } else {
            pkg.setStatus(PackageStatus.ACTIVE);
        }

        return packageRepository.save(pkg);
    }
}