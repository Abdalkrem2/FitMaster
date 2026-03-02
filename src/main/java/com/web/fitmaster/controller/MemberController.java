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

    private final MemberService memberService;


    @PostMapping("/")
    public ResponseEntity<User> createProfile(@RequestBody User user) {
        User createdUser = memberService.createProfile(user);
        return ResponseEntity.ok(createdUser);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody User userDetails) {

        User updatedUser = memberService.updateProfile(userId, userDetails);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        memberService.deleteProfile(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
