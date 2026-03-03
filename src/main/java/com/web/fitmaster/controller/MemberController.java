package com.web.fitmaster.controller;

import com.web.fitmaster.model.User;
import com.web.fitmaster.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PostMapping("/{userId}")
    public ResponseEntity<User> create(@PathVariable Long userId) {
        return ResponseEntity.ok(service.createMember(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(
            @PathVariable Long userId,
            @RequestParam String fullName) {

        return ResponseEntity.ok(service.updateMember(userId, fullName));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable Long userId) {
        return ResponseEntity.ok(service.deleteMember(userId));
    }
}