package com.ybe.ybe_toyproject3.global.error.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@Schema(description = "예외 발생시 리턴 데이터")
public class ErrorResponse {
    @Schema(description = "발생한 http 상태 코드", defaultValue = "에러 상태 코드")
    private Integer errorCode; // http 상태 코드
    @Schema(description = "발생한 예외에 대한 설명", defaultValue = "발생한 예외 메세지에 대한 설명")
    private String message; // 에러 메시지
    @Builder
    public ErrorResponse(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}