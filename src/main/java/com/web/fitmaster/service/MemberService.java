package com.web.fitmaster.service;

import com.web.fitmaster.model.User;

public interface MemberService {

    User createProfile(User user);

    User updateProfile(Long userId, User userDetails);

    void deleteProfile(Long userId);
}
