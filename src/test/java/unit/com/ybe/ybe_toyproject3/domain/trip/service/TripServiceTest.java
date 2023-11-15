package com.ybe.ybe_toyproject3.domain.trip.service;

import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripDetailResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TripServiceTest {

    @Autowired
    private TripService tripService;
    @Autowired
    private TripRepository tripRepository;

    @Test
    @Transactional
    @DisplayName("여행 조회 테스트 -  검색어 없는 경우")
    public void findAllTest() {
        List<Trip> tripList1 = tripRepository.findAll();
        List<TripListResponse> testTripListResponseList = tripService.toTripListResponseList(tripList1);
        List<TripListResponse> tripListResponses = tripService.findAllTrips(null);

        assertEquals(testTripListResponseList.get(0).getTripName(), tripListResponses.get(0).getTripName());
    }

    @Test
    @Transactional
    @DisplayName("여행 조회 테스트 -  검색어 있는 경우")
    public void findAllTestWithSearchCondition() {
        List<Trip> tripList1 = tripRepository.findAllByTripNameContaining("야놀자");
        List<TripListResponse> testTripListResponseList = tripService.toTripListResponseList(tripList1);
        List<TripListResponse> tripListResponses = tripService.findAllTrips("야놀자");

        assertEquals(testTripListResponseList.get(0).getTripName(), tripListResponses.get(0).getTripName());
    }

    @Test
    @Transactional
    @DisplayName("특정 여행 조회 테스트 - 검색어 없는 경우")
    public void findTripById() {
        Optional<Trip> trip = tripRepository.findById(1L);
        TripDetailResponse tripDetailResponse = tripService.getTripById(1L);
        TripDetailResponse testTripDetailResponse = TripDetailResponse.fromEntity(trip.get());

        assertEquals(testTripDetailResponse.getTripName(), tripDetailResponse.getTripName());
    }
}
