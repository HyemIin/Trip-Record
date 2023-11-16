package com.ybe.ybe_toyproject3.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.comment.dto.*;
import com.ybe.ybe_toyproject3.domain.comment.model.Comment;
import com.ybe.ybe_toyproject3.domain.comment.repository.CommentRepository;
import com.ybe.ybe_toyproject3.domain.comment.service.CommentService;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripCreateResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.trip.service.TripService;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.common.annotation.WithMockCustomUser;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {
    @MockBean
    SecurityUtilProvider securityUtil;
    @MockBean
    CommentService commentService;
    @MockBean
    TripService tripService;

    @Autowired
    MockMvc mvc;

    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    TripRepository tripRepository;

    @Autowired
    ObjectMapper objectMapper;

    protected MediaType contentType =
            new MediaType(APPLICATION_JSON.getType(),
                    APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    static final String GET_URL = "/comments";

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 생성 성공 테스트")
    void createComment() throws Exception{
        //given
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();
        trip.updateTripId(1L);

        User user = User.builder()
                .id(1L)
                .name("1L")
                .email("hyemin@email.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();
        trip.addUser(user);
        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .content("댓글1")
                .user(user)
                .build();

        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);

        given(commentService.createComment(any(),eq(1L))).willReturn(CommentCreateResponse.fromEntity(comment));
        //when



        //then
        ResultActions resultActions = mvc.perform(post(GET_URL+"/connection-trips/"+1L)
                        .accept(APPLICATION_JSON)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(commentCreateRequest)))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("사용자별 작성 댓글 조회 성공 테스트")
    void findAllCommentByUserId() throws Exception {
        //given
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();
        trip.updateTripId(1L);

        User user = User.builder()
                .id(1L)
                .name("1L")
                .email("hyemin@email.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();

        trip.addUser(user);
        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .content("댓글1")
                .user(user)
                .build();

        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);

        List<CommentReadResponse> commentReadResponseList = new ArrayList<>();
        given(commentService.findAllCommentByUserId(any())).willReturn(commentReadResponseList);

        //when
        commentReadResponseList.add(CommentReadResponse.fromEntity(comment));

        //then
        ResultActions resultActions = mvc.perform(get(GET_URL+"/connection-users/"+1L)
                        .accept(APPLICATION_JSON)
                        .contentType(contentType))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 수정 성공 테스트")
    void editComment() throws Exception {
        //given
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();
        trip.updateTripId(1L);

        User user = User.builder()
                .id(1L)
                .name("1L")
                .email("hyemin@email.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();
        trip.addUser(user);
        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .id(1L)
                .content("댓글1")
                .user(user)
                .build();

        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);
        CommentUpdateRequest commentUpdateRequest = CommentUpdateRequest.builder()
                .content("수정한 댓글")
                .build();
        Comment updateComment = commentUpdateRequest.toEntity();
        updateComment.addTrip(trip);
        updateComment.addUser(user);

        given(commentService.editComment(eq(1L), any())).willReturn(CommentUpdateResponse.fromEntity(updateComment));

        //when

        //then
        ResultActions resultActions = mvc.perform(put(GET_URL+"/"+1L)
                        .accept(APPLICATION_JSON)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(commentUpdateRequest)))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteComment() throws Exception {
        //given
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();
        trip.updateTripId(1L);

        User user = User.builder()
                .id(1L)
                .name("1L")
                .email("hyemin@email.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();
        trip.addUser(user);
        CommentCreateRequest commentCreateRequest = CommentCreateRequest.builder()
                .id(1L)
                .content("댓글1")
                .user(user)
                .build();

        Comment comment = commentCreateRequest.toEntity();
        comment.addTrip(trip);

        given(commentService.deleteComment(any())).willReturn(comment.getId() + "번 댓글이 삭제되었습니다.");
        //when

        //then
        ResultActions resultActions = mvc.perform(delete(GET_URL+"/"+1L)
                        .accept(APPLICATION_JSON)
                        .contentType(contentType))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }
}