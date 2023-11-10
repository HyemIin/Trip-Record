package com.ybe.ybe_toyproject3.domain.user.controller;

import com.ybe.ybe_toyproject3.domain.user.dto.response.SignUpResponse;
import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.service.UserService;
import com.ybe.ybe_toyproject3.global.common.annotation.FailApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "사용자 API", description = "사용자 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "로그인한 사용자 정보 조회 API", description = "로그인한 사용자 정보 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = UserInfo.class)))
    @FailApiResponses
    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }
}
