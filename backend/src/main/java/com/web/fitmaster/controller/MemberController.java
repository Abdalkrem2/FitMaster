package com.web.fitmaster.controller;


import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.dto.MembershipDTOs;
import com.web.fitmaster.model.Membership;
import com.web.fitmaster.model.User;
import com.web.fitmaster.service.MemberService;
import com.web.fitmaster.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController()
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthUtil authUtil;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<MemberDTOs.MemberResponse> getAllMembers(Pageable pageable, @RequestParam(required = false) String search){
        MemberDTOs.MemberResponse memberUsers=memberService.getAllMembers(pageable,search);
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

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeMembers", memberService.countActiveMembers());
        stats.put("expiringSoon", memberService.countExpiringSoon());
        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/me/profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberDTOs.MemberProfileDTO> updateMyProfile(
            @RequestBody @Valid MemberDTOs.MemberProfileRequest request) {
        User loggedIn = authUtil.loggedInUser();
        MemberDTOs.MemberProfileDTO profile = memberService.upsertMemberProfile(loggedIn.getId(), request);
        return ResponseEntity.ok(profile);
    }
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberDTOs.MemberProfileDTO> getMyProfile() {
        User loggedIn = authUtil.loggedInUser();
        MemberDTOs.MemberProfileDTO profile = memberService.getMemberProfile(loggedIn.getId());
        return ResponseEntity.ok(profile);
    }



    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MemberDTOs.MemberDetailsDTO> getMyDetails() {
        User loggedIn = authUtil.loggedInUser();
        MemberDTOs.MemberDetailsDTO details = memberService.getMemberDetails(loggedIn.getId());
        return ResponseEntity.ok(details);
    }

    @PatchMapping("/me/password")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body) {
        User loggedIn = authUtil.loggedInUser();
        memberService.changePassword(loggedIn.getId(), body.get("currentPassword"), body.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully");
    }






}
