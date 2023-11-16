package com.ybe.ybe_toyproject3.domain.comment.service;

import com.ybe.ybe_toyproject3.domain.comment.dto.*;
import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.comment.repository.CommentRepository;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Transactional

class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtilProvider securityUtil;

    @Mock
    private Trip trip;

    @BeforeEach
    void sampleTrip() {
        trip = Trip.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        trip.updateTripId(1L);
    }
    @Test
    @DisplayName("댓글생성 테스트")
    void createComment() {
        //given
        given(securityUtil.getCurrentUserId()).willReturn(1L);
        Long userId = securityUtil.getCurrentUserId();

        given(userRepository.getUserById(userId)).willReturn(
                User.builder()
                        .id(userId)
                        .name("Jeong")
                        .password("1234")
                        .email("hyemin@naver.com")
                        .authority(Authority.ROLE_USER)
                        .build()
        );
        User createdUser = userRepository.getUserById(userId);

        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .id(1L)
                .content("댓글1")
                .user(createdUser)
                .build();
        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);

        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip));
        given(commentRepository.save(any())).willReturn(comment);

        //when
        CommentCreateResponse commentCreateResponse = commentService.createComment(commentCreateRequest, 1L);
        //then
        assertThat("댓글1").isEqualTo(commentCreateResponse.getContent());
        assertThat(createdUser.getId()).isEqualTo(commentCreateResponse.getUserId());
        assertThat(trip.getId()).isEqualTo(commentCreateResponse.getTripId());
        }

        @Test
        @DisplayName("본인 댓글리스트 조회 테스트")
        void findAllCommentByUserId () {
        //given
            given(securityUtil.getCurrentUserId()).willReturn(1L);
            Long userId = securityUtil.getCurrentUserId();

            given(userRepository.getUserById(userId)).willReturn(
                    User.builder()
                            .id(userId)
                            .name("Jeong")
                            .password("1234")
                            .email("hyemin@naver.com")
                            .authority(Authority.ROLE_USER)
                            .build()
            );
            User createdUser = userRepository.getUserById(userId);

            CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                    .id(1L)
                    .content("댓글1")
                    .user(createdUser)
                    .build();
            Comment comment = commentCreateRequest.toEntity();
            comment.addTrip(trip);

            given(commentRepository.save(comment)).willReturn(comment);
            Comment comment1 = commentRepository.save(comment);
            Comment comment2 = commentRepository.save(comment);
            Comment comment3 = commentRepository.save(comment);
            List<CommentReadResponse> commentReadResponseList = new ArrayList<>();
            List<Comment> commentList = new ArrayList<>();
            commentList.add(comment1);
            commentList.add(comment2);
            commentList.add(comment3);
            commentReadResponseList.add(CommentReadResponse.fromEntity(comment1));
            commentReadResponseList.add(CommentReadResponse.fromEntity(comment2));
            commentReadResponseList.add(CommentReadResponse.fromEntity(comment3));

            given(commentRepository.findAllByUserId(userId)).willReturn(commentList);

        //when
            List<CommentReadResponse> commentReadResponses = commentService.findAllCommentByUserId(1L);

        //then
            assertThat(commentReadResponses.get(0).getUserId()).isEqualTo(1L);
            assertThat(commentReadResponses.size()).isEqualTo(commentList.size());

        }

        @Test
        @DisplayName("본인 댓글 수정 테스트")
        void editComment () {
        //given
            given(securityUtil.getCurrentUserId()).willReturn(1L);
            Long userId = securityUtil.getCurrentUserId();

            given(userRepository.getUserById(userId)).willReturn(
                    User.builder()
                            .id(userId)
                            .name("Jeong")
                            .password("1234")
                            .email("hyemin@naver.com")
                            .authority(Authority.ROLE_USER)
                            .build()
            );
            User createdUser = userRepository.getUserById(userId);

            CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                    .id(1L)
                    .content("댓글1")
                    .user(createdUser)
                    .build();

            CommentUpdateRequest commentUpdateRequest = CommentUpdateRequest.builder()
                    .content("수정 댓글")
                    .build();
            Comment comment = commentCreateRequest.toEntity();
            comment.addTrip(trip);

            given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip));
            given(commentRepository.save(any())).willReturn(comment);
            given(commentRepository.findById(any())).willReturn(Optional.of(comment));

        //when
            CommentCreateResponse commentCreateResponse = commentService.createComment(commentCreateRequest, 1L);
            CommentUpdateResponse commentUpdateResponse = commentService.editComment(1L, commentUpdateRequest);
        //then
            assertThat("수정 댓글").isEqualTo(commentUpdateResponse.getContent());
            assertThat(commentCreateResponse.getContent()).isNotEqualTo(commentUpdateResponse.getContent());
        }

        @Test
        @DisplayName("본인 댓글 삭제 테스트")
        void deleteComment () {
        //given
            given(securityUtil.getCurrentUserId()).willReturn(1L);
            Long userId = securityUtil.getCurrentUserId();

            given(userRepository.getUserById(userId)).willReturn(
                    User.builder()
                            .id(userId)
                            .name("Jeong")
                            .password("1234")
                            .email("hyemin@naver.com")
                            .authority(Authority.ROLE_USER)
                            .build()
            );
            User createdUser = userRepository.getUserById(userId);

            CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                    .id(1L)
                    .content("댓글1")
                    .user(createdUser)
                    .build();
            Comment comment = commentCreateRequest.toEntity();
            comment.addTrip(trip);

            given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip));
            given(commentRepository.save(any())).willReturn(comment);
            given(commentRepository.findById(commentCreateRequest.getId())).willReturn(Optional.of(comment));
        //when
            CommentCreateResponse commentCreateResponse = commentService.createComment(commentCreateRequest, 1L);
            commentService.deleteComment(commentCreateRequest.getId());
        //then
            commentRepository.findById(1L);
            assertEquals(0,commentRepository.count());

        }


}
