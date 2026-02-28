package com.web.fitmaster.controller;

import com.web.fitmaster.model.Package;
import com.web.fitmaster.services.PackageService;
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


    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody Package pkg) {
        Package created = packageService.createPackage(pkg);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackage(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.getPackageById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable Long id,
                                                 @RequestBody Package pkg) {
        Package updated = packageService.updatePackage(id, pkg);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Package> changeStatus(@PathVariable Long id) {
        Package updated = packageService.changeStatus(id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}