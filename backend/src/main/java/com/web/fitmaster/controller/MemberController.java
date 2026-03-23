package com.web.fitmaster.controller;


import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.dto.MembershipDTOs;
import com.web.fitmaster.model.Membership;
import com.web.fitmaster.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<MemberDTOs.MemberDetailsDTO> getMemberDetails(@PathVariable Long id){
        MemberDTOs.MemberDetailsDTO memberDetails=memberService.getMemberDetails(id);
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
    public ResponseEntity<MemberDTOs.MemberDTO> updateMember(@RequestBody MemberDTOs.MemberUpdateRequest memberDTO, @PathVariable Long id){
        MemberDTOs.MemberDTO memberDetails=memberService.updateMember(memberDTO,id);
        return new ResponseEntity<>(memberDetails,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id){
        String status= memberService.deleteMember(id);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PostMapping("/{id}/memberships")
    public void addMembership(@PathVariable Long id,@RequestBody @Valid MembershipDTOs.MembershipRequest request){
      memberService.addMembership(id,request);
    }

    @GetMapping("{id}/memberships")
    public ResponseEntity<List< MembershipDTOs.MembershipHistory>> getMemberships(@PathVariable Long id){
        List<MembershipDTOs.MembershipHistory> response= memberService.getMemberships(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
