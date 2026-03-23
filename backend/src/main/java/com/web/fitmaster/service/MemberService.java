package com.web.fitmaster.service;

import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.dto.MembershipDTOs;
import com.web.fitmaster.model.Membership;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberDTOs.MemberResponse getAllMembers(Pageable pageable);
    MemberDTOs.MemberDetailsDTO getMemberDetails(Long id);

    MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberDTO);
    MemberDTOs.MemberDTO updateMember(MemberDTOs.@Valid MemberUpdateRequest memberDTO,Long id);

    String deleteMember(Long id);

    void addMembership(Long id, MembershipDTOs.MembershipRequest request);

    List<MembershipDTOs.MembershipHistory> getMemberships(Long id);
}
