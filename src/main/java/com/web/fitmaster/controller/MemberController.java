package com.web.fitmaster.controller;


import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController()
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MemberDTOs.MemberResponse> getAllMembers(Pageable pageable){
        MemberDTOs.MemberResponse memberUsers=memberService.getAllMembers(pageable);
        return new ResponseEntity<>(memberUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MemberDTOs.MemberDTO> getMember(@PathVariable Long id){
        MemberDTOs.MemberDTO memberDetails=memberService.getMember(id);
        return new ResponseEntity<>(memberDetails,HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MemberDTOs.MemberDTO> createMember(@Valid @RequestBody MemberDTOs.MemberRequest memberDTO){
        MemberDTOs.MemberDTO member=memberService.createMember(memberDTO);
        return new ResponseEntity<>(member,HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MemberDTOs.MemberDTO> updateMember(@RequestBody MemberDTOs.MemberUpdateRequest memberDTO,Long id){
        MemberDTOs.MemberDTO memberDetails=memberService.updateMember(memberDTO,id);
        return new ResponseEntity<>(memberDetails,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id){
        String status= memberService.deleteMember(id);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
