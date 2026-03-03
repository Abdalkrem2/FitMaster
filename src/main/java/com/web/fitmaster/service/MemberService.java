package com.web.fitmaster.service;

import com.web.fitmaster.model.User;

public interface MemberService {

    User createMember(Long userId);

    User updateMember(Long userId, String fullName);

    String deleteMember(Long userId);
}
