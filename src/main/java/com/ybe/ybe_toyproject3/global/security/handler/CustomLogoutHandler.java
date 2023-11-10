package com.ybe.ybe_toyproject3.global.security.handler;

import com.ybe.ybe_toyproject3.global.common.constants.JwtConstants;
import com.ybe.ybe_toyproject3.global.security.jwt.JwtTokenProvider;
import com.ybe.ybe_toyproject3.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ybe.ybe_toyproject3.global.common.constants.JwtConstants.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("로그아웃 호출됐음");
        setDefaultTargetUrl("http://localhost:8080/auth/logout-redirect");
//        String token = request.getHeader("Authorization").split(" ")[1];
//        log.info("token = " + token);
//        Authentication auth = jwtTokenProvider.getAuthentication(token);
//        User principal = (User) auth.getPrincipal();
//
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//
//        log.info(request.getRequestURI());
        super.onLogoutSuccess(request, response, authentication);
    }
}
