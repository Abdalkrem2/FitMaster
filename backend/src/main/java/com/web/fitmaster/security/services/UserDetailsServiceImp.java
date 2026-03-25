package com.web.fitmaster.security.services;


import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
        @Autowired
        UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user=userRepository.findByPhone(phone).orElseThrow(()->new UsernameNotFoundException("User with username: "+phone+" not found!"));

        return UserDetailsImp.build(user);
    }
}
