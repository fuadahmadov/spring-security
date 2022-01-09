package com.springsecurity.jwt.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.constant.ErrorCode;
import com.springsecurity.dto.ErrorDto;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedHandlerJwt implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException accessDeniedException) throws IOException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorDto response = new ErrorDto(ErrorCode.ACCESS_DENIED);
        byte[] responseToSend = new ObjectMapper().writeValueAsString(response).getBytes(StandardCharsets.UTF_8);
        httpServletResponse.getOutputStream().write(responseToSend);
    }
}
