package com.web.fitmaster.services.Imp;


import com.web.fitmaster.model.Package;
import com.web.fitmaster.model.enums.PackageStatus;
import com.web.fitmaster.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements com.web.fitmaster.services.PackageService {

    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

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
        packageRepository.deleteById(id);
    }

    @Override
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public Package getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
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