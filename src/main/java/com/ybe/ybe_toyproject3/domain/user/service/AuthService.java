package com.ybe.ybe_toyproject3.domain.user.service;

import com.ybe.ybe_toyproject3.domain.user.dto.request.LoginRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.LoginResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.constants.Constants;
import com.ybe.ybe_toyproject3.global.common.constants.JwtConstants;
import com.ybe.ybe_toyproject3.global.security.jwt.JwtTokenProvider;
import com.ybe.ybe_toyproject3.global.security.token.dto.TokenDto;
import com.ybe.ybe_toyproject3.global.security.token.dto.TokenDto.TokenInfoDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.http.HttpHeaders;

import java.util.Optional;

import static com.ybe.ybe_toyproject3.global.common.constants.Constants.REISSUED;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("회원가입되어있는 이메일입니다.");
        }
        User user = signUpRequest.toEntity(passwordEncoder);
        userRepository.save(user);
        return SignUpResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticate(loginRequest);
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication, response);
        HttpHeaders responseHeaders = createAuthorizationHeader(tokenInfoDTO.getAccessToken());
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        return new ResponseEntity<>(LoginResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName()).build(), responseHeaders, HttpStatus.OK);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = loginRequest.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(token);
    }

    private HttpHeaders createAuthorizationHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String username = getCurrentUsername();
        deleteCookie(response);
        return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.getRefreshTokenFromRequest(request);
        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

        HttpHeaders responseHeaders = createAuthorizationHeader(newAccessToken);
        return new ResponseEntity<>(REISSUED, responseHeaders, HttpStatus.OK);
    }

}
