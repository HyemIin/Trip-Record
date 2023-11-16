package com.ybe.ybe_toyproject3.domain.trip.service;

import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripCreateResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTestMock {

    @InjectMocks
    private TripService tripService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private ItineraryRepository itineraryRepository;

    @Mock
    private SecurityUtilProvider securityUtil;

    private static final String SEARCH_CONDITION = "일본";

    @Test
    @DisplayName("여행생성 테스트")
    public void createTripTest() {
        //given
        TripCreateRequest tripCreateRequest = TripCreateRequest.builder()
                .tripName("여행1")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest.toEntity();

        given(securityUtil.getCurrentUserId()).willReturn(1L);
        long userId = securityUtil.getCurrentUserId();
        assertEquals(1L, userId);

        given(userRepository.getUserById(userId)).willReturn(
                User.builder()
                        .id(1L)
                        .name("cha")
                        .password("1234")
                        .email("cdm2883@naver.com")
                        .authority(Authority.ROLE_USER)
                        .build()
        );
        User createdUser = userRepository.getUserById(1L);
        assertEquals("cha", createdUser.getName());
        trip.addUser(createdUser);

        ArgumentCaptor<Trip> captor = ArgumentCaptor.forClass(Trip.class);

        //when
        TripCreateResponse tripCreateResponse = tripService.createTrip(tripCreateRequest);

        //then
        verify(tripRepository, times(1))
                .save(captor.capture());

        Trip savedTrip = captor.getValue();
        assertEquals("여행1", savedTrip.getTripName());
        assertEquals(DOMESTIC, savedTrip.getTripType());
    }

    @Test
    @DisplayName("여행조회 테스트")
    public void findAllTripTest() throws Exception {
        TripCreateRequest tripCreateRequest1 = TripCreateRequest.builder()
                .tripName("일본")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        TripCreateRequest tripCreateRequest2 = TripCreateRequest.builder()
                .tripName("중국")
                .tripStartDate(LocalDateTime.now().minusDays(10))
                .tripEndDate(LocalDateTime.now().minusDays(1))
                .tripType(DOMESTIC)
                .build();
        Trip trip = tripCreateRequest1.toEntity();
        Trip trip2 = tripCreateRequest2.toEntity();
        User user = User.builder()
                .id(1L)
                .name("cha")
                .email("cdm2883@naver.com")
                .password("1234")
                .authority(Authority.ROLE_USER)
                .build();

        trip.addUser(user);
        trip2.addUser(user);

        List<Trip> tripList = List.of(trip, trip2);


        List<TripListResponse> tripListResponseList = new ArrayList<>();

        for (var t : tripList) {
            TripListResponse tripListResponse = TripListResponse.fromEntity(t);
            tripListResponseList.add(tripListResponse);
        }
        assertEquals(2, tripListResponseList.size());

        when(tripRepository.findAll()).thenReturn(tripList);
        when(tripRepository.findAllByTripNameContaining(SEARCH_CONDITION))
                .thenReturn(tripList.stream()
                        .filter(t -> t.getTripName().startsWith(SEARCH_CONDITION)||
                                t.getTripName().endsWith(SEARCH_CONDITION))
                        .toList());

        List<TripListResponse> tripListResponses = tripService.findAllTrips(null);
        List<TripListResponse> tripListResponsesWithSearchCondition = tripService.findAllTrips(SEARCH_CONDITION);

        assertEquals(2, tripListResponses.size());
        assertEquals(1, tripListResponsesWithSearchCondition.size());
        System.out.println(tripListResponsesWithSearchCondition.stream().map(TripListResponse::getTripName));
//
    }
}
