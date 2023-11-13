package com.ybe.ybe_toyproject3.domain.likes.service;

import com.ybe.ybe_toyproject3.domain.likes.dto.TripLikesCreateResponse;
import com.ybe.ybe_toyproject3.domain.likes.exception.AlreadyExistLikesException;
import com.ybe.ybe_toyproject3.domain.likes.exception.NullLikesException;
import com.ybe.ybe_toyproject3.domain.likes.exception.NullUserLikesException;
import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import com.ybe.ybe_toyproject3.domain.likes.repository.LikesRepository;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.exception.UserNotFoundException;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ybe.ybe_toyproject3.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public TripLikesCreateResponse createLikes(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new TripNotFoundException(NO_TRIP.getMessage()));

        //헤더에 있는 토큰 가지고 userId 받기
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(NO_USER.getMessage())
        );

        Optional<Likes> foundLikes = likesRepository.findByTripIdAndUserId(tripId, userId);
        if (foundLikes.isPresent()) {
            throw new AlreadyExistLikesException(ALREADY_EXIST_LIKES.getMessage());
        }

        Likes likes = new Likes(trip, user);
        Likes saved = likesRepository.save(likes);
        return TripLikesCreateResponse.getTripLikesCreateResponse(saved);
    }

    @Transactional
    public void deleteLikes(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new TripNotFoundException(NO_TRIP.getMessage()));

        //헤더에 있는 토큰 가지고 userId 받기
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(NO_USER.getMessage())
        );

        Optional<Likes> foundLikes = likesRepository.findByTripIdAndUserId(tripId, userId);
        if (foundLikes.isEmpty()) {
            throw new NullLikesException(NO_LIKES.getMessage());
        }

        Long likesId = foundLikes.get().getId();
        likesRepository.deleteById(likesId);
    }

    @Transactional
    public List<TripListResponse> getTripsUserLikes() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(NO_USER.getMessage())
        );

        List<Likes> likesList = user.getLikesList();
        if (likesList.isEmpty()) {
            throw new NullUserLikesException(NO_TRIP_USER_LIKES.getMessage());
        }

        List<TripListResponse> tripListResponse = new ArrayList<>();
        for (Likes likes:likesList) {
            Trip trip = likes.getTrip();
            TripListResponse tripResponse = TripListResponse.fromEntity(trip);
            tripListResponse.add(tripResponse);
        }

        return tripListResponse;
    }
}
