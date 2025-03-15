package com.example.jwt.user.dto;

import com.example.jwt.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleDto {

    private String role;

    public RoleDto(UserRole role) {
        this.role = role.name();
    }
}

