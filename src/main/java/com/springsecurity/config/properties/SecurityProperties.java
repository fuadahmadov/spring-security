package com.springsecurity.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "security")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityProperties {

    JwtProperties jwt = new JwtProperties();
    CorsConfiguration cors = new CorsConfiguration();

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JwtProperties {
        String secret;
        long accessTokenValidityInMinutes;
        long refreshTokenValidityInMinutes;
    }

}
