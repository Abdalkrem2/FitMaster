package com.web.fitmaster.controller;

import com.web.fitmaster.dto.PackageDTOs;
import com.web.fitmaster.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<List<PackageDTOs .PackageDTO>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String > deletePackage(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.deletePackage(id));
    }

    @PostMapping
    public ResponseEntity<PackageDTOs.PackageDTO> createPackage(@RequestBody PackageDTOs.CreatePackageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.createPackage(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageDTOs.PackageDTO> updatePackage(@PathVariable Long id, @RequestBody PackageDTOs.CreatePackageRequest req) {
        return ResponseEntity.ok(packageService.updatePackage(id,req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDTOs.PackageDTO> getPackage(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.getPackage(id));
    }
}
