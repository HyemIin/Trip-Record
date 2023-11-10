package com.ybe.ybe_toyproject3.domain.user.controller;

import com.ybe.ybe_toyproject3.domain.user.dto.request.LoginRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.request.SignUpRequest;
import com.ybe.ybe_toyproject3.domain.user.dto.response.LoginResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.service.AuthService;
import com.ybe.ybe_toyproject3.domain.user.service.UserService;
import com.ybe.ybe_toyproject3.global.common.annotation.FailApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 인증 API", description = "사용자 인증 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공시", content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    @FailApiResponses
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @Operation(summary = "로그인 API", description = "회원 로그인 API 입니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @FailApiResponses
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                                        HttpServletResponse response) {
        ResponseEntity<LoginResponse> loginResponse = authService.login(loginRequest, response);
        HttpHeaders headers = loginResponse.getHeaders(); // 기존 헤더 가져오기
        return ResponseEntity.status(loginResponse.getStatusCode()).headers(headers).body(loginResponse.getBody());
    }

    @Operation(summary = "로그아웃 API", description = "회원 로그아웃 API 입니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @FailApiResponses
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<String> logoutResponse = authService.logout(request, response);
        HttpHeaders headers = logoutResponse.getHeaders(); // 기존 헤더 가져오기
        return ResponseEntity.status(logoutResponse.getStatusCode()).headers(headers).body(logoutResponse.getBody());
    }
}
