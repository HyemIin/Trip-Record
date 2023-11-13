package com.ybe.ybe_toyproject3.domain.comment.dto;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommentCreateRequest {

    @NotNull
    private String content;
//    @NotNull
    private User user;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .user(user)
                .build();
    }
}
