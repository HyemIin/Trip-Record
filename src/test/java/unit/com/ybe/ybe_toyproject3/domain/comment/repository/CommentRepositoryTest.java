package com.ybe.ybe_toyproject3.domain.comment.repository;

import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.user.dto.response.UserInfo;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("댓글을 사용자ID별로 찾는 성공 테스트")
    @Test
    @WithMockCustomUser
    void findAllByUserId() {
        //given
        UserInfo userInfo = UserInfo.builder()
                .id(1L)
                .email("testuser@email.com")
                .name("testuser")
                .build();
        //when
        List<Comment> commentList = commentRepository.findAllByUserId(userInfo.getId());
        int count = 0;
        for (Comment comment : commentList) {
            if (comment.getUser().getId() == userInfo.getId()) {
                count += 1;
            }
        }
        //then
        assertThat(count).isEqualTo(commentList.size());

    }
}