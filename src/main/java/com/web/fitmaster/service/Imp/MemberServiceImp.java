package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.service.MemberService;

public class MemberServiceImp implements MemberService {

    private MemberDTOs.MemberDTO member;


    @Override
    public MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberRequest) {
        member = new MemberDTOs.MemberDTO(
                1L,
                memberRequest.getName(),
                memberRequest.getPhone()
        );
        return member;
    }


    @Override
    public MemberDTOs.MemberDTO updateMember(Long id, MemberDTOs.MemberRequest memberRequest) {
        if (member == null || !member.getId().equals(id)) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        member.setName(memberRequest.getName());
        member.setPhone(memberRequest.getPhone());
        return member;
    }



    @Override
    public String deleteMember(Long id) {

        if (member == null || !member.getId().equals(id)) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        member = null;

        return "Member with id= " + id + " was deleted successfully";
    }
}