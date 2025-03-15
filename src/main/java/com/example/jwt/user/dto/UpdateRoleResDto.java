package com.example.jwt.user.dto;

import com.example.jwt.global.enums.UserRole;
import com.example.jwt.user.dto.RoleDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UpdateRoleResDto {

    private String username;
    private String nickname;
    private List<RoleDto> roles;


    public UpdateRoleResDto(String username, String nickname, UserRole role) {
        this.username = username;
        this.nickname = nickname;
        this.roles = List.of(new RoleDto(role));
    }
}

