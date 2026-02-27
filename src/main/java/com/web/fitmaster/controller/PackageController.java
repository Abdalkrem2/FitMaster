package com.web.fitmaster.controller;

import com.web.fitmaster.model.Package;
import com.web.fitmaster.services.PackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    public Package createPackage(@RequestBody Package pkg) {
        return packageService.createPackage(pkg);
    }
    //response entity for all + postman

    @GetMapping
    public List<Package> getAllPackages() {
        return packageService.getAllPackages();
    }

    @GetMapping("/{id}")
    public Package getPackage(@PathVariable Long id) {
        return packageService.getPackageById(id);
    }

    @PutMapping("/{id}")
    public Package updatePackage(@PathVariable Long id,
                                 @RequestBody Package pkg) {
        return packageService.updatePackage(id, pkg);
    }

    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
    }

    @PatchMapping("/{id}/status")
    public Package changeStatus(@PathVariable Long id) {
        return packageService.changeStatus(id);
    }
}