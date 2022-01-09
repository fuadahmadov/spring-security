package com.springsecurity.jwt.service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springsecurity.config.properties.SecurityProperties;
import com.springsecurity.exception.InvalidTokenException;
import com.springsecurity.jwt.constant.TokenField;
import com.springsecurity.jwt.constant.TokenType;
import com.springsecurity.jwt.model.UserDetail;
import com.springsecurity.mapper.AuthMapper;
import com.springsecurity.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static com.springsecurity.constant.Common.AUTH_HEADER;
import static com.springsecurity.constant.Common.BEARER_AUTH_HEADER;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    Key key;
    long accessTokenValidity;
    long refreshTokenValidity;
    final SecurityProperties properties;

    @PostConstruct
    private void init() {
        accessTokenValidity = 60 * 1000 * properties.getJwt().getAccessTokenValidityInMinutes();
        refreshTokenValidity = 60 * 1000 * properties.getJwt().getRefreshTokenValidityInMinutes();
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    private String createToken(Authentication authentication, TokenType tokenType, Long validityInMs) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        List<String> roles = grantedAuthorityToRole(authentication);
        Date validity = DateUtil.epochMillisToDate(validityInMs);

        return Jwts.builder()
                .claim(TokenField.TOKEN_TYPE, tokenType)
                .claim(TokenField.USER_ID, userDetail.getId())
                .claim(TokenField.USER_EMAIL, userDetail.getEmail())
                .claim(TokenField.USER_ROLES, roles)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    private List<String> grantedAuthorityToRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, TokenType.ACCESS, accessTokenValidity);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, TokenType.REFRESH, refreshTokenValidity);
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException
                | MalformedJwtException
                | ExpiredJwtException
                | UnsupportedJwtException
                | IllegalArgumentException e) {
            throw new InvalidTokenException("Jwt parse error : Invalid jwt token");
        }
    }


    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(this::isBearerAuth)
                .map(this::getToken)
                .flatMap(this::getAuthentication);
    }

    private boolean isBearerAuth(String header) {
        return header.toLowerCase().startsWith(BEARER_AUTH_HEADER.toLowerCase());
    }

    private Optional<String> getToken(String header) {
        return Optional.of(header.substring(BEARER_AUTH_HEADER.length()).trim());
    }

    private Optional<Authentication> getAuthentication(Optional<String> token) {
        if (token.isPresent()) {
            Claims claims = parseToken(token.get());
            return Optional.of(AuthMapper.claimsToAuthentication(claims));
        }
        return Optional.empty();
    }
}
