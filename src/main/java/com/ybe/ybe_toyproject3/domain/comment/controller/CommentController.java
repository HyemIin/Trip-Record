package com.ybe.ybe_toyproject3.domain.comment.controller;

import com.ybe.ybe_toyproject3.domain.comment.dto.*;
import com.ybe.ybe_toyproject3.domain.comment.service.CommentService;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripUpdateResponse;
import com.ybe.ybe_toyproject3.global.common.annotation.FailApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "댓글 API", description = "댓글 관련 API 모음입니다.")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성 API", description = "댓글 생성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공시",
            content = @Content(schema = @Schema(implementation = ItineraryCreateResponse.class)))
    @FailApiResponses
    @PostMapping("/connection-trips/{tripId}")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long tripId,
            @RequestBody @Valid CommentCreateRequest commentCreateRequest) {
        return ResponseEntity.ok(commentService.createComment(commentCreateRequest,tripId));
    }

    @Operation(summary = "사용자별 댓글 조회 API", description = "각 사용자별 댓글을 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공 시", content = @Content(schema = @Schema(implementation = TripListResponse.class)))
    @FailApiResponses
    @GetMapping("/connection-users/{userId}")
    public ResponseEntity<List<CommentReadResponse>> findAllCommentByUserId() {
        return ResponseEntity.ok(commentService.findAllCommentByUserId());
    }
    @Operation(summary = "댓글 수정 API", description = "특정 댓글을 수정하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공 시", content = @Content(schema = @Schema(implementation = TripUpdateResponse.class)))
    @FailApiResponses
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> editComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.ok(commentService.editComment(commentId,commentUpdateRequest));
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공시", content = @Content(schema = @Schema(implementation = TripResponse.class)))
    @FailApiResponses
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));

    }
}
