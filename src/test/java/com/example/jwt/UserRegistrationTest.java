package com.example.jwt;

import com.example.jwt.global.enums.UserRole;
import com.example.jwt.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    // 테스트용 사용자 데이터 초기화
    @BeforeEach
    public void setUp() {
        // 새로운 사용자 객체 생성
        testUser = new User("username", "Abc1234!", "Test Nickname", UserRole.USER);
    }

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        // User 객체 JSON으로 변환
        String requestJson = objectMapper.writeValueAsString(testUser);

        // POST 요청을 통해 사용자 등록 API 테스트
        mockMvc.perform(post("/signup") // 회원가입 API 호출
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.nickname").value("Test Nickname"));
    }

    @Test
    public void shouldReturnBadRequestForMissingUsername() throws Exception {

        testUser = new User("", "Abc1234!", "Test Nickname", UserRole.USER);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"createdAt\":null,\"modifiedAt\":null,\"id\":null,\"username\":\"\",\"password\":\"password123\",\"nickname\":\"Test Nickname\",\"role\":\"USER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));

    }

    @Test
    public void shouldReturnBadRequestForInvalidPassword() throws Exception {
        // 규칙에 맞지 않는 비밀번호 설정
        testUser = new User("username", "abc", "Test Nickname", UserRole.USER);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"abc\",\"nickname\":\"Test Nickname\",\"role\":\"USER\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));

    }
}
