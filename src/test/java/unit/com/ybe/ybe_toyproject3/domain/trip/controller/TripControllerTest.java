package com.ybe.ybe_toyproject3.domain.trip.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.trip.service.TripService;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripControllerTest.class)
public class TripControllerTest {

    @MockBean
    TripService tripService;
    @Autowired
    MockMvc mvc;
    @MockBean
    TripRepository tripRepository;
    @Autowired
    ObjectMapper objectMapper;

    protected MediaType contentType =
            new MediaType(APPLICATION_JSON.getType(),
                    APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);

    static final String GET_URL = "/trips";

    @Test
    @WithMockCustomUser
    @DisplayName("조회 컨트롤러 테스트")
    public void getTripByIdTest_controller() throws Exception {
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();
        User user = User.builder()
                .id(1L)
                .name("cha")
                .email("cdm2883@naver.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();

        trip.addUser(user);

        List<Trip> tripList = List.of(trip);

        List<TripListResponse> tripListResponseList = new ArrayList<>();

        for (var t : tripList){
            TripListResponse tripListResponse = TripListResponse.fromEntity(t);
            tripListResponseList.add(tripListResponse);
        }


        given(tripService.toTripListResponseList(tripList))
                .willReturn(tripListResponseList);


        given(tripService.findAllTrips(null))
                .willReturn(tripListResponseList);


        List<TripListResponse> tripListResponses = tripService.findAllTrips(null);

        mvc.perform(get(GET_URL).contentType(contentType))
                //.andExpect(status().isOk())
                .andDo(print());


    }
}
