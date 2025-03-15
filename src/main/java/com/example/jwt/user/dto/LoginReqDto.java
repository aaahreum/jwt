package com.example.jwt.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginReqDto {

    @NotBlank(message = "username은 필수 항목 입니다.")
    private String username;

    @NotBlank(message = "password는 필수 항목 입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    public LoginReqDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

