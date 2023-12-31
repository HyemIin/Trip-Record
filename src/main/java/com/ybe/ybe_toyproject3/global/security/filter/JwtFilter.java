package com.ybe.ybe_toyproject3.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.global.error.response.ErrorResponse;
import com.ybe.ybe_toyproject3.global.security.jwt.JwtTokenProvider;
import com.ybe.ybe_toyproject3.global.security.token.exception.ExpireAccessTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ybe.ybe_toyproject3.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static com.ybe.ybe_toyproject3.global.common.constants.JwtConstants.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

//    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info(request.getMethod() + " " + request.getRequestURI());
        try {
            // 1. request Header에서 토큰 꺼냄, 여기서 HTTP ONLY 쿠키에서 읽어오게 변경 가능
            String jwt = resolveToken(request);
            // 2. validateToken으로 유효성 검사
            // 정상 토큰이면, Authentication을 가져와서 SecurityContext에 저장
            if (jwt != null) {
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    User user = (User) authentication.getPrincipal();
                    if (user.getUsername() != null) {
                        log.info("memberId : " + user.getUsername());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    log.error("만료된 엑세스 토큰이다");
                    throw new ExpireAccessTokenException();
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpireAccessTokenException e) {
            log.error(e.getMessage());
            log.error(e.getClass().getName());
            String result = mapper.writeValueAsString(new ErrorResponse(401, e.getMessage()));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

            // response.setHeader("Access-Control-Allow-Origin", "https://www.recordyslow.com");
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getClass().getName());
            e.printStackTrace();
            String result = mapper.writeValueAsString(new ErrorResponse(409, e.getMessage()));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // response.setHeader("Access-Control-Allow-Origin", "https://www.recordyslow.com");
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String actualToken = bearerToken.substring(7);
            log.info("Extracted Token: " + actualToken);
            return actualToken;
        }
        return null;
    }
}