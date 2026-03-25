package com.web.fitmaster.security.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String jwtToken;
    private String phone;
    private List<String> roles;

    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.phone = username;
        this.roles = roles;

    }
}
