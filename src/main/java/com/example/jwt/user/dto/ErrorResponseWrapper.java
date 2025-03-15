package com.example.jwt.user.dto;

import com.example.jwt.user.dto.ErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseWrapper {

    private final ErrorResponseDto error;
}

