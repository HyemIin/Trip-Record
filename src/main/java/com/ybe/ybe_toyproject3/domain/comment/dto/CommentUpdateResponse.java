package com.ybe.ybe_toyproject3.domain.comment.dto;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateResponse {

    @NotNull
    private Long commentId;
    @NotNull
    private String content;
    @NotNull
    private Long tripId;
    @NotNull
    private User user;

    @Builder
    public CommentUpdateResponse(Long commentId, String content, Long tripId,User user) {
        this.commentId = commentId;
        this.content = content;
        this.tripId = tripId;
        this.user = user;
    }

    public static CommentUpdateResponse fromEntity(Comment comment) {
        return CommentUpdateResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .tripId(comment.getTrip().getId())
//                .userId(comment.getUser().getId())
                .build();
    }
}
