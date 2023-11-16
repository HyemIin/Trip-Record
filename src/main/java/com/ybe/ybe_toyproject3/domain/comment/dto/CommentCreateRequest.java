package com.ybe.ybe_toyproject3.domain.comment.dto;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class CommentCreateRequest {
    private Long id;
    @NotNull
    private String content;
//    @NotNull
    private User user;

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .build();
    }
}
