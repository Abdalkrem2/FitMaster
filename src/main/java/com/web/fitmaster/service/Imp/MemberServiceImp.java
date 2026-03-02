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
    public User createProfile(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateProfile(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        return userRepository.save(user);
    }

    @Override
    public void deleteProfile(Long userId) {
        userRepository.deleteById(userId);
    }
}
