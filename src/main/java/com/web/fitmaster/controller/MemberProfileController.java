package com.web.fitmaster.controller;

import com.web.fitmaster.dto.MemberProfileDTOs;
import com.web.fitmaster.service.MemberProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    // ------------------- GET ALL (PAGINATION) -------------------
    @GetMapping
    public ResponseEntity<MemberProfileDTOs.MemberProfileResponse> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MemberProfileDTOs.MemberProfileResponse response =
                memberProfileService.getAllProfilesResponse(PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    // ------------------- CREATE -------------------
    @PostMapping("/{userId}")
    public ResponseEntity<MemberProfileDTOs.MemberProfileDTO> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody MemberProfileDTOs.MemberProfileRequest request
    ) {
        MemberProfileDTOs.MemberProfileDTO profile = memberProfileService.createProfile(userId, request);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    // ------------------- UPDATE -------------------
    @PutMapping("/{profileId}")
    public ResponseEntity<MemberProfileDTOs.MemberProfileDTO> updateProfile(
            @PathVariable Long profileId,
            @RequestBody MemberProfileDTOs.MemberProfileUpdateRequest request
    ) {
        MemberProfileDTOs.MemberProfileDTO profile = memberProfileService.updateProfile(profileId, request);
        return ResponseEntity.ok(profile);
    }

    // ------------------- DELETE -------------------
    @DeleteMapping("/{profileId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long profileId) {
        memberProfileService.deleteProfile(profileId);
        return ResponseEntity.ok("Profile deleted successfully");
    }

    // ------------------- GET SINGLE -------------------
    @GetMapping("/{profileId}")
    public ResponseEntity<MemberProfileDTOs.MemberProfileDTO> getProfile(@PathVariable Long profileId) {
        MemberProfileDTOs.MemberProfileDTO profile = memberProfileService.getMemberProfile(profileId);
        return ResponseEntity.ok(profile);
    }
}
