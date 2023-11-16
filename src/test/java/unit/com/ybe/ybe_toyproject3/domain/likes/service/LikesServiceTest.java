package com.ybe.ybe_toyproject3.domain.likes.service;

import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import com.ybe.ybe_toyproject3.domain.likes.repository.LikesRepository;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import com.ybe.ybe_toyproject3.global.util.SecurityUtilProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;
    @Mock
    private LikesRepository likesRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private SecurityUtilProvider securityUtilImpl;

    @Test
    @DisplayName("좋아요 생성 테스트")
    @Transactional
    void createLikes() {
        //given
        Long tripId = 1L;
        given(tripRepository.findById(1L)).willReturn(
                Optional.ofNullable(Trip.builder()
                        .tripName("trip1")
                        .tripStartDate(LocalDateTime.of(2024, 11, 12, 12, 0))
                        .tripEndDate(LocalDateTime.of(2024, 11, 12, 12, 0))
                        .tripType(TripType.DOMESTIC)
                        .build())
        );
        Trip trip = tripRepository.findById(tripId).orElseThrow(IllegalArgumentException::new);

        given(securityUtilImpl.getCurrentUserId()).willReturn(1L);
        long userId = securityUtilImpl.getCurrentUserId();
        assertThat(userId).isEqualTo(1L);

        given(userRepository.findById(1L)).willReturn(
                Optional.ofNullable(User.builder()
                        .id(1L)
                        .name("user1")
                        .password("1234")
                        .email("user1@mail.com")
                        .authority(Authority.ROLE_USER)
                        .build())
        );
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        given(likesRepository.findByTripIdAndUserId(tripId, userId)).willReturn(Optional.empty());

        given(likesRepository.save(any(Likes.class))).willReturn(
                Likes.builder()
                        .trip(trip)
                        .user(user)
                        .build()
        );

        ArgumentCaptor<Likes> captor = ArgumentCaptor.forClass(Likes.class);

        //when
        likesService.createLikes(tripId);

        //then
        verify(likesRepository, times(1)).save(captor.capture());

        Likes savedLikes = captor.getValue();
        assertThat(savedLikes.getTrip()).isEqualTo(trip);
        assertThat(savedLikes.getUser()).isEqualTo(user);
//        assertThatThrownBy(()->likesService.createLikes(tripId)).isInstanceOf(AlreadyExistLikesException.class);
    }

    @Test
    @DisplayName("좋아요 삭제 테스트")
    @Transactional
    void deleteLikes() {

        //given
        Long tripId = 1L;
        given(tripRepository.findById(1L)).willReturn(
                Optional.ofNullable(Trip.builder()
                        .tripName("trip1")
                        .tripStartDate(LocalDateTime.of(2024, 11, 12, 12, 0))
                        .tripEndDate(LocalDateTime.of(2024, 11, 12, 12, 0))
                        .tripType(TripType.DOMESTIC)
                        .build())
        );
        Trip trip = tripRepository.findById(tripId).orElseThrow(IllegalArgumentException::new);

        given(securityUtilImpl.getCurrentUserId()).willReturn(1L);
        long userId = securityUtilImpl.getCurrentUserId();
        assertThat(userId).isEqualTo(1L);

        given(userRepository.findById(1L)).willReturn(
                Optional.ofNullable(User.builder()
                        .id(1L)
                        .name("user1")
                        .password("1234")
                        .email("user1@mail.com")
                        .authority(Authority.ROLE_USER)
                        .build())
        );
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        given(likesRepository.findByTripIdAndUserId(tripId, userId)).willReturn(
                Optional.ofNullable(Likes.builder()
                        .user(user)
                        .trip(trip)
                        .build())
        );

        Likes likes = likesRepository.findByTripIdAndUserId(tripId, userId).orElseThrow(IllegalArgumentException::new);
        Long likesId = likes.getId();

        //when
        likesService.deleteLikes(tripId);

        //then
        verify(likesRepository, times(1)).deleteById(likesId);
    }

    @Test
    @DisplayName("유저가 좋아요한 여행 목록 조회 테스트")
    @Transactional
    void getTripsUserLikes() {
        //given
        given(securityUtilImpl.getCurrentUserId()).willReturn(1L);
        long userId = securityUtilImpl.getCurrentUserId();
        assertThat(userId).isEqualTo(1L);

        given(userRepository.findById(1L)).willReturn(
                Optional.ofNullable(User.builder()
                        .id(1L)
                        .name("user1")
                        .password("1234")
                        .email("user1@mail.com")
                        .authority(Authority.ROLE_USER)
                        .build())
        );
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        List<Likes> likesList = new ArrayList<>();
        Trip trip1 = new Trip("trip1",
                LocalDateTime.of(2024,11,12,12,0),
                LocalDateTime.of(2024,11,12,12,0),
                TripType.DOMESTIC);
        trip1.addUser(user);
        likesList.add(Likes.builder().trip(trip1).user(user).build());
        Trip trip2 = new Trip("trip2",
                LocalDateTime.of(2024,11,12,12,0),
                LocalDateTime.of(2024,11,12,12,0),
                TripType.DOMESTIC);
        trip2.addUser(user);
        likesList.add(Likes.builder().trip(trip2).user(user).build());
        user.addLikes(likesList);

        //when
        List<TripListResponse> tripsUserLikes = likesService.getTripsUserLikes();

        //then
        assertThat(tripsUserLikes.size()).isEqualTo(likesList.size());
        assertThat(tripsUserLikes.get(0).getTripName()).isEqualTo("trip1");
        assertThat(tripsUserLikes.get(1).getTripName()).isEqualTo("trip2");
    }
}