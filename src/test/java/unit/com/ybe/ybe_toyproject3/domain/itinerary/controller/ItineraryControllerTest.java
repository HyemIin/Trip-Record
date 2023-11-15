package com.ybe.ybe_toyproject3.domain.itinerary.controller;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryCreateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.service.ItineraryService;
import com.ybe.ybe_toyproject3.domain.location.dto.LocationResponse;

import com.ybe.ybe_toyproject3.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class ItineraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItineraryService itineraryService;

    @InjectMocks
    private ItineraryController itineraryController;

    @Test
    void createItineraryTest() throws Exception {
        ItineraryCreateResponse expectedResponse = ItineraryCreateResponse.builder()
                .itineraryId(1L)
                .itineraryName("여정 A")
                .transportation("이동 수단 A")
                .departCity("도시 A")
                .arriveCity("도시 B")
                .cityDepartTime(LocalDateTime.parse("2023-10-25T10:00:00"))
                .cityArriveTime(LocalDateTime.parse("2023-10-25T12:00:00"))
                .accommodation("호텔 A")
                .checkInTime(LocalDateTime.parse("2023-10-25T14:00:00"))
                .checkOutTime(LocalDateTime.parse("2023-10-26T12:00:00"))
                .placeName("장소 A")
                .placeArriveTime(LocalDateTime.parse("2023-10-25T14:00:00"))
                .placeDepartTime(LocalDateTime.parse("2023-10-25T13:00:00"))
                .location(LocationResponse.builder()
                        .locationId(1L)
                        .locationName("위치 A")
                        .build())
                .build();
        when(itineraryService.createItinerary(anyLong(), any(ItineraryCreateRequest.class)))
                .thenReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/trips/{tripId}/itinerary", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"itineraryName\" : \"여정 A\", \"transportation\" : \"이동 수단 A\", \"departCity\": \"도시 A\", \"arriveCity\": \"도시 B\", \"cityDepartTime\": \"2023-10-25T10:00:00\", \"cityArriveTime\": \"2023-10-25T12:00:00\", \"accommodation\": \"호텔 A\", \"checkInTime\": \"2023-10-25T14:00:00\", \"checkOutTime\": \"2023-10-26T12:00:00\", \"placeName\": \"장소 A\", \"placeArriveTime\": \"2023-10-25T14:00:00\", \"placeDepartTime\": \"2023-10-25T13:00:00\" }")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itineraryId").value(1L))
                .andExpect(jsonPath("$.itineraryName").value("여정 A"))
                .andExpect(jsonPath("$.transportation").value("이동 수단 A"))
                .andExpect(jsonPath("$.departCity").value("도시 A"))
                .andExpect(jsonPath("$.arriveCity").value("도시 B"))
                .andExpect(jsonPath("$.cityDepartTime").value("2023-10-25T10:00:00"))
                .andExpect(jsonPath("$.cityArriveTime").value("2023-10-25T12:00:00"))
                .andExpect(jsonPath("$.accommodation").value("호텔 A"))
                .andExpect(jsonPath("$.checkInTime").value("2023-10-25T14:00:00"))
                .andExpect(jsonPath("$.checkOutTime").value("2023-10-26T12:00:00"))
                .andExpect(jsonPath("$.placeName").value("장소 A"))
                .andExpect(jsonPath("$.placeArriveTime").value("2023-10-25T14:00:00"))
                .andExpect(jsonPath("$.placeDepartTime").value("2023-10-25T13:00:00"))
                .andExpect(jsonPath("$.location.locationId").value(1L))
                .andExpect(jsonPath("$.location.locationName").value("위치 A"));
    }

    @Test
    void deleteItineraryTest() throws Exception {
        String deletedItineraryId = "1";

        when(itineraryService.deleteItinerary(anyLong()))
                .thenReturn(deletedItineraryId);

        // when & then
        mockMvc.perform(delete("/trips/{tripId}/itinerary/{itineraryId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deletedItineraryId));
    }
}