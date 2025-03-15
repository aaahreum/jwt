package com.example.jwt.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupReqDto {

    @NotBlank(message = "username은 필수 항목 입니다.")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "password는 필수 항목 입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Size(max = 100)
    private String password;

    private String nickname;
}
