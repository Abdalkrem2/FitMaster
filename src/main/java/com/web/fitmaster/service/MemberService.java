package com.web.fitmaster.service;

import com.web.fitmaster.dto.MemberDTOs;

import jakarta.validation.Valid;


public interface MemberService {
    MemberDTOs.MemberDTO createMember(@Valid MemberDTOs.MemberRequest Request);
    MemberDTOs.MemberDTO updateMember(Long id, MemberDTOs.MemberUpdate request);

    String deleteMember(Long id);

}
