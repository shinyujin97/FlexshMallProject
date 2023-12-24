package com.javalab.boot.config.auth;

import com.javalab.boot.domain.user.User;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 로그인을 하게되면 로그인 객체 (User) 를 저장하는 역할
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Log4j2
public class PrincipalDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private User user;



    public PrincipalDetails(User user){
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> collector = new ArrayList<>();
        collector.add(new SimpleGrantedAuthority(user.getRole().getRole()));

        return collector;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
