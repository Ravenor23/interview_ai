package com.habsida.interview_ai.model;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ADMIN,
    USER,
    ANONYMOUS;

    @Override
    public String getAuthority() {
        return name();
    }
}
