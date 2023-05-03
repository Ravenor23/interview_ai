package com.habsida.interview_ai.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationRequest {
    private String email;
    private String password;
}
