package com.springsecurity.service;

import java.util.HashSet;
import java.util.Set;

import com.springsecurity.constant.RoleName;
import com.springsecurity.domain.Role;
import com.springsecurity.domain.User;
import com.springsecurity.dto.LoginRequestDto;
import com.springsecurity.dto.SignupRequestDto;
import com.springsecurity.exception.InvalidTokenException;
import com.springsecurity.exception.InvalidUserCredentialException;
import com.springsecurity.exception.RegisterAlreadyExistException;
import com.springsecurity.exception.RoleNotFoundException;
import com.springsecurity.jwt.constant.TokenField;
import com.springsecurity.jwt.model.JwtTokenDto;
import com.springsecurity.jwt.service.JwtService;
import com.springsecurity.mapper.AuthMapper;
import com.springsecurity.repository.RoleRepository;
import com.springsecurity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    JwtService jwtService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public JwtTokenDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidUserCredentialException("Login error : Email not found"));
        boolean isPassTrue = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPassTrue)
            throw new InvalidUserCredentialException("Login error : Password isn't correct");

        Authentication authentication = AuthMapper.userToAuthentication(user);
        String accessToken = jwtService.createAccessToken(authentication);
        String refreshToken = jwtService.createRefreshToken(authentication);
        user.setToken(refreshToken);

        userRepository.save(user);
        return new JwtTokenDto(accessToken, refreshToken);
    }


    public void signup(SignupRequestDto request) {
        boolean isUserExist = userRepository.existsByEmail(request.getEmail());
        if (isUserExist)
            throw new RegisterAlreadyExistException("Singup error : Email was found");

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setBirthday(request.getBirthday());

        Set<RoleName> defaultRoles = Set.of(RoleName.ROLE_USER, RoleName.ROLE_MODERATOR);
        Set<Role> roles = new HashSet<>();

        defaultRoles.forEach(r -> {
            Role role = roleRepository.findByName(r)
                    .orElseThrow(() -> new RoleNotFoundException("Signup error : Role wasn't found."));
            roles.add(role);
        });

        user.setRoles(roles);
        userRepository.save(user);
    }

    public JwtTokenDto refreshToken(String token) {
        Claims claims = jwtService.parseToken(token);
        String email = claims.get(TokenField.USER_EMAIL, String.class);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserCredentialException("Refresh token error : Email not found"));

        if (!user.getToken().equals(token))
            throw new InvalidTokenException("Refresh token error : Token isn't last token");

        Authentication authentication = AuthMapper.userToAuthentication(user);
        String accessToken = jwtService.createAccessToken(authentication);
        String refreshToken = jwtService.createRefreshToken(authentication);
        user.setToken(refreshToken);
        userRepository.save(user);

        return new JwtTokenDto(accessToken, refreshToken);
    }

}
