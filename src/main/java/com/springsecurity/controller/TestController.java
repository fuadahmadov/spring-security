package com.springsecurity.controller;

import javax.annotation.security.RolesAllowed;

import com.springsecurity.constant.RoleName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/public") // no authentication or authorization
    public String publicc() {
        return "Hello World Public";
    }

    @GetMapping("/hello") // only authentication no authorization
    public Object hello(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/user") // authentication and only users are authorized to access
    public Object user(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/admin") // authentication and only admins are authorized to access
    public Object admin(Authentication authentication) {
        return authentication;
    }

    @RolesAllowed({RoleName.Fields.ROLE_MODERATOR})
    @GetMapping("/moderator") // authentication and only moderators are authorized to access
    public Object moderator(Authentication authentication) {
        return authentication;
    }

}
