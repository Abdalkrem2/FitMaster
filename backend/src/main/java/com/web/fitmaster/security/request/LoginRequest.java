package com.web.fitmaster.security.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LoginRequest {
    private String phone;
    private String password;

}
