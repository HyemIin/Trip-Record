package com.ybe.ybe_toyproject3.domain.comment.dto;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentReadResponse {
    @NotNull
    private Long commentId;
    @NotNull
    private String content;
    @NotNull
    private Long tripId;
    @NotNull
    private Long userId;

    @Builder
    public CommentReadResponse(Long commentId, String content, Long tripId, Long userId) {
        this.commentId = commentId;
        this.content = content;
        this.tripId = tripId;
        this.userId = userId;
    }

    public static CommentReadResponse fromEntity(Comment comment) {
        return CommentReadResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .tripId(comment.getTrip().getId())
                .userId(comment.getUser().getId())
                .build();
    }
}
