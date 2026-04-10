package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MemberProfileDTOs;
import com.web.fitmaster.model.MemberProfile;
import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.GoalPriority;
import com.web.fitmaster.repository.MemberProfileRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.MemberProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class MemberProfileServiceImp implements MemberProfileService {

    private final MemberProfileRepository repository;
    private final UserRepository userRepository;

    public MemberProfileServiceImp(MemberProfileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // ------------------- GET BY ID -------------------
    @Override
    public MemberProfileDTOs.MemberProfileDTO getMemberProfile(Long profileId) {
        MemberProfile profile = repository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToDTO(profile);
    }

    // ------------------- CREATE -------------------
    @Override
    public MemberProfileDTOs.MemberProfileDTO createProfile(Long userId, MemberProfileDTOs.MemberProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MemberProfile profile = new MemberProfile();
        profile.setUser(user);
        profile.setWeight(request.getWeight());
        profile.setHeight(request.getHeight());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGoalPriority(GoalPriority.valueOf(request.getGoalPriority()));

        return mapToDTO(repository.save(profile));
    }

    // ------------------- UPDATE -------------------
    @Override
    public MemberProfileDTOs.MemberProfileDTO updateProfile(Long profileId, MemberProfileDTOs.MemberProfileUpdateRequest request) {
        MemberProfile profile = repository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (request.getWeight() != null) profile.setWeight(request.getWeight());
        if (request.getHeight() != null) profile.setHeight(request.getHeight());
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getGoalPriority() != null) profile.setGoalPriority(GoalPriority.valueOf(request.getGoalPriority()));

        return mapToDTO(repository.save(profile));
    }

    // ------------------- DELETE -------------------
    @Override
    public void deleteProfile(Long profileId) {
        MemberProfile profile = repository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        repository.delete(profile);
    }

    // ------------------- GET ALL (PAGINATION) -------------------

    public Page<MemberProfileDTOs.MemberProfileDTO> getAllProfiles(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    // ------------------- GET ALL AS RESPONSE DTO -------------------

    public MemberProfileDTOs.MemberProfileResponse getAllProfilesResponse(Pageable pageable) {
        Page<MemberProfile> page = repository.findAll(pageable);

        List<MemberProfileDTOs.MemberProfileDTO> content = page.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();

        return MemberProfileDTOs.MemberProfileResponse.builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    // ------------------- MAPPING METHOD -------------------
    private MemberProfileDTOs.MemberProfileDTO mapToDTO(MemberProfile profile) {
        return MemberProfileDTOs.MemberProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .weight(profile.getWeight())
                .height(profile.getHeight())
                .dateOfBirth(profile.getDateOfBirth())
                .goalPriority(profile.getGoalPriority() != null ? profile.getGoalPriority().name() : null)
                .age(calculateAge(profile.getDateOfBirth()))
                .bmi(calculateBMI(profile.getWeight(), profile.getHeight()))
                .build();
    }

    // ------------------- HELPER METHODS -------------------
    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private double calculateBMI(Double weight, Double height) {
        if (weight == null || height == null || height == 0) return 0;
        return weight / (height * height);
    }
}