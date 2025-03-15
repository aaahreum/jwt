package com.example.jwt;

import com.example.jwt.global.enums.UserRole;
import com.example.jwt.user.dto.LoginReqDto;
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
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("username", "Abc1234!", "Test Nickname", UserRole.USER);
    }

    @Test
    public void shouldLoginUserSuccessfully() throws Exception {
        LoginReqDto loginReqDto = new LoginReqDto("username", "Abc1234!");

        // JSON으로 변환
        String requestJson = objectMapper.writeValueAsString(loginReqDto);

        // POST 요청을 통해 로그인 API 테스트
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"wronguser\",\"password\":\"Abc1234!\"}"))
                .andExpect(status().isUnauthorized()) // 401으로 수정
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));

    }

    @Test
    public void shouldReturnBadRequestForInvalidUsername() throws Exception {
        // 잘못된 username 설정
        LoginReqDto loginReqDto = new LoginReqDto("wronguser", "Abc1234!");

        String requestJson = objectMapper.writeValueAsString(loginReqDto);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    public void shouldReturnBadRequestForInvalidPassword() throws Exception {
        // 잘못된 password 설정
        LoginReqDto loginReqDto = new LoginReqDto("username", "wrongpassword");

        String requestJson = objectMapper.writeValueAsString(loginReqDto);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest()) // 400으로 수정
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));

    }
}
