package com.ybe.ybe_toyproject3.domain.trip.service;

import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripCreateResponse;
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

import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
}
