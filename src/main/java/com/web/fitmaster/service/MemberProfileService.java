package com.web.fitmaster.service;

import com.web.fitmaster.dto.MemberProfileDTOs;

import org.springframework.data.domain.Pageable;

public interface MemberProfileService {


    MemberProfileDTOs.MemberProfileDTO createProfile(Long userId, MemberProfileDTOs.MemberProfileRequest request);


    MemberProfileDTOs.MemberProfileDTO updateProfile(Long profileId, MemberProfileDTOs.MemberProfileUpdateRequest request);


    void deleteProfile(Long profileId);


    MemberProfileDTOs.MemberProfileDTO getMemberProfile(Long profileId);
    MemberProfileDTOs.MemberProfileResponse getAllProfilesResponse(Pageable pageable);


}