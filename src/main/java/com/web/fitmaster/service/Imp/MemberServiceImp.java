package com.web.fitmaster.service.Imp;

import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final UserRepository userRepository;


    @Override
    public User createMember(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userRepository.save(user);
    }

    @Override
    public User updateMember(Long userId, String fullName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(fullName);

        return userRepository.save(user);
    }

    @Override
    public String deleteMember(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.save(user);

        return "Member role removed";
    }
}