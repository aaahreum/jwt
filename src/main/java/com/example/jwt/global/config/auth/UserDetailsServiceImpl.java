package com.example.jwt.global.config.auth;

import com.example.jwt.user.entity.User;
import com.example.jwt.user.repository.UserRepository;
import com.example.jwt.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 입력받은 유저이름에 해당하는 사용자 정보 조회
     *
     * @param username 유저이름
     * @return 해당 사용자의 UserDetailsImpl 객체
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저이름입니다."));
        return new UserDetailsImpl(user);
    }
}

