package com.web.fitmaster.controller;

import com.web.fitmaster.repository.RoleRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.security.jwt.JwtUtils;
import com.web.fitmaster.security.request.LoginRequest;
import com.web.fitmaster.security.response.LoginResponse;
import com.web.fitmaster.security.response.MessageResponse;
import com.web.fitmaster.security.services.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword())
            );
        }catch (Exception e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad credentials");
            map.put("status",false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();

        String jwtCookie=jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        LoginResponse response=new LoginResponse(userDetails.getId(),jwtCookie,userDetails.getUsername(),roles);

        return ResponseEntity
                .ok(response);

    }

    @GetMapping("/username")
    public   ResponseEntity<?> currentUserName(Authentication authentication){
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return new ResponseEntity<>(authentication.getName(),HttpStatus.OK) ;
    }



    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        LoginResponse response=new LoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(response);
    }


//    @PostMapping("/signout")
//    public ResponseEntity<?> signOutUser(){
//
//        ResponseCookie cookie=jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("Signed out successfully"));
//    }

}
