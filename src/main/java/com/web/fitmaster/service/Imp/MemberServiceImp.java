package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.service.MemberService;
import jakarta.validation.Valid;

import java.awt.print.Pageable;

public class MemberServiceImp implements MemberService {
    @Override
    public MemberDTOs.MemberResponse getAllMembers(Pageable pageable) {
        return null;
    }

    @Override
    public MemberDTOs.MemberDTO getMember(Long id) {
        return null;
    }

    @Override
    public MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberDTO) {
        return null;
    }

    @Override
    public MemberDTOs.MemberDTO updateMember(MemberDTOs.@Valid MemberUpdateRequest memberDTO, Long id) {
        return null;
    }

    @Override
    public String deleteMember(Long id) {
        return "";
    }
}
