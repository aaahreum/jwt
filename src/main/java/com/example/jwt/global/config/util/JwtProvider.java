package com.example.jwt.global.config.util;

import com.example.jwt.user.entity.User;
import com.example.jwt.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    /**
     * JWT 시크릿 키
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 토큰 만료시간(밀리초)
     */
    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    private final UserRepository userRepository;

    /**
     * 토큰 생성 후 리턴
     *
     * @param authentication 인증 완료된 후 세부 정보
     * @return 생성된 토큰
     * @throws EntityNotFoundException 입력받은 이메일에 해당하는 사용자를 찾지 못했을 경우
     */
    public String generateToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();
        return this.generateTokenBy(username);
    }

    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    /**
     * 토큰이 유효한지 확인
     *
     * @param token 토큰
     * @return 유효 여부
     */
    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        }

        return false;
    }

    /**
     * 유저이름 이용해 토큰을 생성한 후 리턴
     * 토큰 생성에는 HS256 알고리즘 이용
     *
     * @param username 유저이름
     * @return 생성된 토큰
     * @throws EntityNotFoundException 입력받은 유저이름에 해당하는 사용자를 찾지 못했을 경우
     */
    private String generateTokenBy(String username) throws EntityNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("해당 username에 맞는 값이 존재하지 않습니다."));
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.expiryMillis);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT의 claim 부분 추출
     *
     * @param token 토큰
     */
    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 입력받은 토큰의 만료 여부
     *
     * @param token 토큰
     * @return 만료 여부
     */
    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 입력 받은 토큰의 만료일 리턴
     *
     * @param token 토큰
     * @return 만료일
     */
    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    /**
     * 토큰에 입력 받은 로직을 적용하고 그 결과를 리턴
     *
     * @param token          토큰
     * @param claimsResolver 토큰에 적용할 로직.
     * @param <T>            {@code claimsResolver}의 리턴 타입.
     * @return {@code T}
     */
    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }
}

