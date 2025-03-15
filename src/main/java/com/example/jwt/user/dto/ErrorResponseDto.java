package com.example.jwt.user.dto;

import com.example.jwt.global.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private final String code;
    private final String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}

