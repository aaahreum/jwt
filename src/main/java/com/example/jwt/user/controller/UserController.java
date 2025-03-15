package com.example.jwt.user.controller;

import com.example.jwt.user.dto.LoginReqDto;
import com.example.jwt.user.dto.LoginResDto;
import com.example.jwt.user.dto.SignupReqDto;
import com.example.jwt.user.dto.SignupResDto;
import com.example.jwt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "회원가입 및 로그인 기능 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 가입된 사용자",
                    content = @Content(schema = @Schema(example = "{ 'error': { 'code': 'USER_ALREADY_EXISTS', 'message': '이미 가입된 사용자입니다.' } }")))
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResDto> userSignup(
            @Valid @RequestBody SignupReqDto signupReqDto
    ) {
        SignupResDto userSignup = userService.userSignup(signupReqDto.getUsername(), signupReqDto.getPassword(), signupReqDto.getNickname());

        return new ResponseEntity<>(userSignup, HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 아이디 또는 비밀번호",
                    content = @Content(schema = @Schema(example = "{ 'error': { 'code': 'INVALID_CREDENTIALS', 'message': '아이디 또는 비밀번호가 올바르지 않습니다.' } }")))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(
            @Valid @RequestBody LoginReqDto loginReqDto) {
        LoginResDto authResponse = userService.userLogin(loginReqDto.getUsername(), loginReqDto.getPassword());

        return ResponseEntity.ok(authResponse);
    }

}

