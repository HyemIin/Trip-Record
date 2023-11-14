package com.ybe.ybe_toyproject3.domain.likes.controller;

import com.ybe.ybe_toyproject3.domain.likes.dto.TripLikesCreateResponse;
import com.ybe.ybe_toyproject3.domain.likes.service.LikesService;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripResponse;
import com.ybe.ybe_toyproject3.global.common.annotation.FailApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
public class LikesController {
    private final LikesService likesService;

    @Operation(summary = "좋아요 생성 API", description = "좋아요 생성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = TripLikesCreateResponse.class)))
    @FailApiResponses
    @PostMapping("/{tripId}/likes")
    public ResponseEntity<TripLikesCreateResponse> createLikes(@PathVariable Long tripId) {
        return ResponseEntity.ok(likesService.createLikes(tripId));
    }

    @Operation(summary = "좋아요 삭제 API", description = "좋아요 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공시", content = @Content(schema = @Schema(implementation = TripLikesCreateResponse.class)))
    @FailApiResponses
    @DeleteMapping("/{tripId}/likes")
    public ResponseEntity<String> deleteLikes(@PathVariable Long tripId) {
        likesService.deleteLikes(tripId);
        return ResponseEntity.ok(tripId+" 여행의 좋아요가 취소되었습니다");
    }

    @Operation(summary = "사용자가 누른 좋아요 여행 조회 API", description = "사용자가 누른 좋아요 여행 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = TripResponse.class)))
    @FailApiResponses
    @GetMapping("/user/liked")
    public ResponseEntity<List<TripListResponse>> getTripsUserLikes() {
        return ResponseEntity.ok(likesService.getTripsUserLikes());
    }
}
