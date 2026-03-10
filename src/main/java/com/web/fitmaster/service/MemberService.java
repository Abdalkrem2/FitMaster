package com.web.fitmaster.service;

import com.web.fitmaster.dto.MemberDTOs;
import jakarta.validation.Valid;

import java.awt.print.Pageable;

public interface MemberService {
    MemberDTOs.MemberResponse getAllMembers(Pageable pageable);
    MemberDTOs.MemberDTO getMember(Long id);

    MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberDTO);
    MemberDTOs.MemberDTO updateMember(MemberDTOs.@Valid MemberUpdateRequest memberDTO,Long id);

    String deleteMember(Long id);
}
