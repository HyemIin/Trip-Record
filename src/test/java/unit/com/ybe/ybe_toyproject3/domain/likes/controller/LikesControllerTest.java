package com.ybe.ybe_toyproject3.domain.likes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.likes.dto.TripLikesCreateResponse;
import com.ybe.ybe_toyproject3.domain.likes.service.LikesService;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikesController.class)
@AutoConfigureMockMvc(addFilters = false)
class LikesControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    LikesService likesService;
    @Autowired
    ObjectMapper objectMapper;

    static final String LIKES_URL = "/trip";

    @Test
    @DisplayName("좋아요 생성 api 테스트")
    void createLikes() throws Exception {

        //given
        given(likesService.createLikes(1L)).willReturn(TripLikesCreateResponse.builder()
                .tripId(1L)
                .userId(1L)
                .build());

        //when&then
        mvc.perform(post(LIKES_URL + "/{tripId}/likes", 1)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("tripId").value(1))
                .andExpect(jsonPath("userId").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("좋아요 삭제 api 테스트")
    void deleteLikes() throws Exception {

        //when&then
        mvc.perform(delete(LIKES_URL + "/{tripId}/likes", 1)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().bytes("1 여행의 좋아요가 취소되었습니다".getBytes(StandardCharsets.UTF_8)))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자가 누른 좋아요 여행 조회 API 테스트")
    void getTripsUserLikes() throws Exception {

        //given
        List<TripListResponse> tripListResponses = List.of(
                TripListResponse.builder()
                        .id(1L)
                        .userId(1L)
                        .build()
        );
        given(likesService.getTripsUserLikes()).willReturn(tripListResponses);

        //when&then
        mvc.perform(get(LIKES_URL+"/user/liked")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tripListResponses)))
                .andDo(print());

    }
}