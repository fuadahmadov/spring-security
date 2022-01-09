package com.springsecurity.jwt.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    public UserAuthentication(Long id,
                              String email,
                              Collection<? extends GrantedAuthority> authorities) {
        super(new UserDetail(id, email), "", authorities);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "";
    }

    @Override
    @JsonIgnore
    public Object getDetails() {
        return "";
    }

}
