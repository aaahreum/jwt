package com.example.jwt.global.config.auth;

import com.example.jwt.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import com.example.jwt.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getRole();

        return new ArrayList<>(role.getAuthorities());
    }

    /**
     * 사용자의 자격 증명 반환
     *
     * @return 암호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자의 자격 증명 반환
     *
     * @return 사용자 이름
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정 만료
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명 만료
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화
     *
     * @return 사용 여부
     * @apiNote 사용할 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

