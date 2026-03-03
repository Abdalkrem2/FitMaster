package com.web.fitmaster.service;

import com.web.fitmaster.dto.MemberDTOs;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public interface MemberService {
    MemberDTOs.MemberDTO createMember(@Valid MemberDTOs.MemberRequest memberRequest);

    MemberDTOs.MemberDTO updateMember(Long id, MemberDTOs.MemberRequest memberRequest);

     String deleteMember(Long id);

}
