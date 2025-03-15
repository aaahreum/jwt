package com.example.jwt.user.service;

import com.example.jwt.global.config.util.AuthenticationScheme;
import com.example.jwt.global.config.util.JwtProvider;
import com.example.jwt.global.enums.UserRole;
import com.example.jwt.user.dto.LoginResDto;
import com.example.jwt.user.dto.SignupResDto;
import com.example.jwt.user.dto.UpdateRoleResDto;
import com.example.jwt.user.entity.User;
import com.example.jwt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResDto userSignup(String username, String password, String nickname) {
        Optional<User> optionalUser  = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, nickname, UserRole.USER);
        User savedUser = userRepository.save(user);

        return new SignupResDto(
                savedUser.getUsername(),
                savedUser.getNickname(),
                savedUser.getRole()
        );
    }

    public LoginResDto userLogin(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        // 아이디가 존재하지 않는 경우
        if (user.isEmpty()) {
            throw new AuthenticationException("아이디 또는 비밀번호가 올바르지 않습니다.") {};
        }

        // 아이디는 존재하지만 비밀번호가 틀린 경우
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationException("아이디 또는 비밀번호가 올바르지 않습니다.") {};
        }

        validatePassword(password, user.get().getPassword());

        // 사용자 인증 후 인증 객체를 저장
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 생성
        String accessToken = this.jwtProvider.generateToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) throws IllegalArgumentException {
        boolean notValid = !this.passwordEncoder.matches(rawPassword, encodedPassword);

        if (notValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다.");
        }
    }

    public UpdateRoleResDto updateRole(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        user.updateRole();
        User savedUser = userRepository.save(user);

        return new UpdateRoleResDto(
                savedUser.getUsername(),
                savedUser.getNickname(),
                savedUser.getRole()
        );
    }
}