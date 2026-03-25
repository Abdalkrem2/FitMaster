package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.PackageDTOs;
import com.web.fitmaster.exceptions.APIException;
import com.web.fitmaster.model.Package;
import com.web.fitmaster.model.enums.PackageStatus;
import com.web.fitmaster.repository.PackageRepository;
import com.web.fitmaster.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImp implements PackageService {
    private final PackageRepository packageRepository;

    @Override
    public List<PackageDTOs.PackageDTO> getAllPackages() {
        List<Package> pkg= packageRepository.findAll();
        return pkg.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String deletePackage(Long id) {
        if (!packageRepository.existsById(id)) {
            throw new RuntimeException("Package not found");
        }
        packageRepository.deleteById(id);
        return "Package with id= "+id+" was deleted successfully";
    }

    @Override
    public PackageDTOs. PackageDTO createPackage(PackageDTOs.CreatePackageRequest req) {
        Package pkg= new Package();
        pkg.setName(req.getName());
        pkg.setDescription(req.getDescription());
        pkg.setPrice(req.getPrice());
        pkg.setStatus(req.getStatus());
        pkg.setDurationInDays(req.getDurationInDays());
        pkg = packageRepository.save(pkg);
        return mapToDto(pkg);
    }

    @Override
    public PackageDTOs.PackageDTO updatePackage(Long id, PackageDTOs.UpdatePackageRequest req) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new APIException("Package with id= "+id+" not found"));
        if (req.getName() != null)
            pkg.setName(req.getName());
        if (req.getDescription() != null)
            pkg.setDescription(req.getDescription());
        if (req.getPrice() != null)
            pkg.setPrice(req.getPrice());
        if (req.getDurationInDays() != null)
            pkg.setDurationInDays(req.getDurationInDays());
        Package updatedPkg = packageRepository.save(pkg);
        return mapToDto(updatedPkg);
    }

    @Override
    public PackageDTOs.@Nullable PackageDTO getPackage(Long id) {
      Package pkg=packageRepository.findById(id).orElseThrow(() -> new APIException("Package with id= "+id+" not found"));
        return mapToDto(pkg);
    }

    @Override
    public PackageDTOs.@Nullable PackageDTO updateStatus(Long id, PackageDTOs.UpdatePackageStatus status) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new APIException("Not found"));

        pkg.setStatus(status.getStatus());
        return mapToDto(packageRepository.save(pkg));
    }


    private PackageDTOs.PackageDTO mapToDto(Package pkg) {
        return PackageDTOs.PackageDTO.builder()
                .id(pkg.getPackageId())
                .name(pkg.getName())
                .description(pkg.getDescription())
                .price(pkg.getPrice())
                .durationInDays(pkg.getDurationInDays())
                .status(pkg.getStatus())
                .build();
    }
}
