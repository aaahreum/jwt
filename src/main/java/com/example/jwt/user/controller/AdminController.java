package com.example.jwt.user.controller;

import com.example.jwt.user.dto.UpdateRoleResDto;
import com.example.jwt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "관리자 API", description = "관리자 권한을 부여하는 API")
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(
            summary = "관리자 권한 부여",
            description = "USER 권한을 ADMIN으로 변경하는 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 변경 성공",
                    content = @Content(schema = @Schema(example = "{ 'username': 'tester', 'nickname': '닉네임', 'roles': { 'role': 'ADMIN' } }"))
            ),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(example = "{ 'error': { 'code': 'USER_NOT_FOUND', 'message': '사용자를 찾을 수 없습니다.' } }"))
            ),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음",
                    content = @Content(schema = @Schema(example = "{ 'error': { 'code': 'ACCESS_DENIED', 'message': '관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.' } }"))
            )
    })
    @PatchMapping("/users/{userId}/roles")
    public ResponseEntity<UpdateRoleResDto> updateRole(@PathVariable Long userId) {
        UpdateRoleResDto updateRole = userService.updateRole(userId);

        return new ResponseEntity<>(updateRole, HttpStatus.OK);
    }
}

