package com.example.jwt.global.config.filter;

import com.example.jwt.global.config.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private static final String[] WHITE_LIST = {"/signup", "/login", "/h2/**","/swagger-ui/**"};

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 화이트리스트에 포함된 경로는 JWT 토큰 검증을 건너뛴다.
        if (isWhiteListed(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticate(request);
        filterChain.doFilter(request, response);
    }

    private boolean isWhiteListed(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String whiteListedPath : WHITE_LIST) {
            if (requestURI.startsWith(whiteListedPath)) {
                return true;  // 화이트리스트에 해당하면 true 반환
            }
        }
        return false;  // 화이트리스트에 해당하지 않으면 false
    }

    private void authenticate(HttpServletRequest request) {
        // 토큰 검증
        String token = getTokenFromRequest(request);
        if (token == null || !jwtProvider.validToken(token)) {
            return;
        }

        // 토큰에서 유저이름 가져옴
        String username = this.jwtProvider.getUsername(token);

        // username에 해당되는 사용자를 찾아옴
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // SecurityContext에 인증 객체 저장
        this.setAuthentication(request, userDetails);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = "Bearer ";

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix)) {
            return bearerToken.substring(headerPrefix.length());
        }

        return null;
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}