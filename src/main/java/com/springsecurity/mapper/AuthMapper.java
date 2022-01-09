package com.springsecurity.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.springsecurity.domain.User;
import com.springsecurity.jwt.constant.TokenField;
import com.springsecurity.jwt.model.UserAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthMapper {
    public static Authentication userToAuthentication(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getName().name()))
                .collect(Collectors.toList());
        return new UserAuthentication(user.getId(), user.getEmail(), authorities);
    }

    public static Authentication claimsToAuthentication(Claims claims) {
        Long id = claims.get(TokenField.USER_ID, Long.class);
        String email = claims.get(TokenField.USER_EMAIL, String.class);
        List<?> roles = claims.get(TokenField.USER_ROLES, List.class);
        List<GrantedAuthority> authorities = roles
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.toString()))
                .collect(Collectors.toList());
        return new UserAuthentication(id, email, authorities);
    }
}
