package com.ybe.ybe_toyproject3.domain.Itinerary.service;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryCreateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import com.ybe.ybe_toyproject3.domain.itinerary.service.ItineraryService;
import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;

import com.ybe.ybe_toyproject3.domain.location.model.Location;
import com.ybe.ybe_toyproject3.domain.location.repository.LocationRepository;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;

import static com.ybe.ybe_toyproject3.global.common.Authority.ROLE_USER;
import static com.ybe.ybe_toyproject3.global.common.type.TripType.DOMESTIC;

import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;

import com.ybe.ybe_toyproject3.global.component.KakaoApiComponent;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class ItineraryServiceTest {
    @Mock
    private SecurityUtilProvider securityUtil;

    @Mock
    private KakaoApiComponent kakaoApiComponent;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private ItineraryRepository itineraryRepository;
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    private Long userId;
    private Long tripId;
    private Long itineraryId;

    @BeforeEach
    void setUp() {
        when(securityUtil.getCurrentUserId()).thenReturn(1L);

        userId = securityUtil.getCurrentUserId();
        User testUser = User.builder()
                .name("user A")
                .password("password A")
                .email("userA@email.com")
                .authority(ROLE_USER)
                .build();
        testUser.setId(userId);
        when(userRepository.getUserById(userId)).thenReturn(testUser);

        tripId = 1L;
        Trip testTrip = Trip.builder()
                .tripName("여행 A")
                .tripStartDate(LocalDateTime.parse("2023-11-25T08:00:00"))
                .tripEndDate(LocalDateTime.parse("2023-11-30T08:00:00"))
                .tripType(DOMESTIC)
                .build();
        testTrip.addUser(testUser);
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(testTrip));

        Location testLocation = Location.builder()
                .locationName("위치 A")
                .build();

        itineraryId = 1L;
        Itinerary testItinerary = Itinerary.builder()
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
                .placeDepartTime(LocalDateTime.parse("2023-10-25T12:00:00"))
                .placeArriveTime(LocalDateTime.parse("2023-10-25T13:00:00"))
                .trip(testTrip)
                .location(testLocation)
                .build();
        when(itineraryRepository.findById(itineraryId)).thenReturn(Optional.of(testItinerary));
    }

    @Test
    void createItineraryTest() {
        // given
        ItineraryCreateRequest request = ItineraryCreateRequest.builder()
                .itineraryName("여정 A")
                .transportation("이동 수단 A")
                .departCity("도시 A")
                .arriveCity("도시 B")
                .cityDepartTime(LocalDateTime.parse("2023-11-25T10:00:00"))
                .cityArriveTime(LocalDateTime.parse("2023-11-25T12:00:00"))
                .accommodation("호텔 A")
                .checkInTime(LocalDateTime.parse("2023-11-25T14:00:00"))
                .checkOutTime(LocalDateTime.parse("2023-11-26T12:00:00"))
                .placeName("장소 A")
                .placeDepartTime(LocalDateTime.parse("2023-11-25T12:00:00"))
                .placeArriveTime(LocalDateTime.parse("2023-11-25T13:00:00"))
                .build();

        String placeName = request.getPlaceName();
        when(kakaoApiComponent.getLocationNameByPlaceName(placeName)).thenReturn("위치 A");

        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            return itinerary;
        });

        // when
        ItineraryCreateResponse response = itineraryService.createItinerary(tripId, request);

        // then
        assertNotNull(response);

        assertEquals(request.getItineraryName(), response.getItineraryName());
        assertEquals(request.getAccommodation(), response.getAccommodation());
    }

    @Test
    void deleteItineraryTest() {
        // when
        String deletedMessage = itineraryService.deleteItinerary(itineraryId);

        // then
        assertNotNull(deletedMessage);
    }
}