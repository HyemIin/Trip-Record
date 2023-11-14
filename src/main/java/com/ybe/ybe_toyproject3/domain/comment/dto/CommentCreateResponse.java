package com.ybe.ybe_toyproject3.domain.comment.dto;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateResponse {
    @NotNull
    @Schema(description = "댓글 아이디", defaultValue = "1")
    private Long commentId;
    @NotNull
    private String content;
    @NotNull
    private Long tripId;
    @NotNull
    private Long userId;

    @Builder
    public CommentCreateResponse(Long commentId, String content, Long tripId,Long userId) {
        this.commentId = commentId;
        this.content = content;
        this.tripId = tripId;
        this.userId = userId;
    }

    public static CommentCreateResponse fromEntity(Comment comment) {
        return CommentCreateResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .tripId(comment.getTrip().getId())
                .userId(comment.getUser().getId())
                .build();
    }

}
