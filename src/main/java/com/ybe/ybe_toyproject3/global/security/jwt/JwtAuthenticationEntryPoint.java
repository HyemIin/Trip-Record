package com.ybe.ybe_toyproject3.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error(String.valueOf(authException.getClass()));
        log.error(authException.getMessage());
        sendResponse(request, response, authException);
    }

    private void sendResponse(HttpServletRequest request, HttpServletResponse response,
                              AuthenticationException authException) throws IOException {
        String result = "서버 에러가 발생했습니다";
        if (authException instanceof BadCredentialsException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(409, "잘못된 이메일, 비밀번호 입니다."));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404,
                    "존재하지 않는 멤버입니다."));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404,
                    "인증되지 않은 사용자 입니다."));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.getWriter().write(result);
    }
}