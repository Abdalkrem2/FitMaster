package com.web.fitmaster.util;


import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.security.services.UserDetailsImp;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {


   private final UserRepository userRepository;

    private Authentication auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("No authenticated user");
        }
        return authentication;
    }

    public String loggedInPhone() {
        Authentication authentication = auth();

        // If your principal is UserDetailsImp, get it directly (fast, no DB hit)
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImp userDetails) {
            return userDetails.getUsername(); // this should be phone
        }
        return authentication.getName();
    }

    public User loggedInUser() {
        String phone = loggedInPhone();
        return userRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));
    }

    public Long loggedInUserId() {
        return loggedInUser().getId();
    }


}