package com.webflux.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String role;
    private final String roleName;

}
