package com.ybe.ybe_toyproject3.domain.trip.service;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripUpdateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripCreateResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripListResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripResponse;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripUpdateResponse;
import com.ybe.ybe_toyproject3.domain.trip.exception.DuplicateTripNameException;
import com.ybe.ybe_toyproject3.domain.trip.exception.InvalidTripScheduleException;
import com.ybe.ybe_toyproject3.domain.trip.exception.NullTripListException;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ybe.ybe_toyproject3.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {
    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;

    @Transactional
    public TripCreateResponse createTrip(TripCreateRequest tripCreateRequest) {
        if (tripCreateRequest.getTripStartDate().isAfter(tripCreateRequest.getTripEndDate()) || tripCreateRequest.getTripStartDate().isEqual(tripCreateRequest.getTripEndDate())) {
            throw new InvalidTripScheduleException();
        }
        Trip trip = tripCreateRequest.toEntity();

        Long currentUserId = SecurityUtil.getCurrentUserId();
        User user = userRepository.getUserById(currentUserId);
        trip.setUser(user);

        tripRepository.save(trip);
        return TripCreateResponse.getTripCreateResponse(trip);
    }

    @Transactional
    public List<TripListResponse> findAllTrips() {
        List<Trip> tripList = tripRepository.findAll();

        if (tripList.isEmpty()) {
            throw new TripNotFoundException(NO_TRIP.getMessage());
        }

        List<TripListResponse> tripListResponse = new ArrayList<>();
        for (Trip trip : tripList) {
            List<String> itineraryNames = trip.getItineraryList()
                    .stream()
                    .map(Itinerary::getItineraryName)
                    .collect(Collectors.toList());

            TripListResponse tripResponse = TripListResponse.builder()
                    .userId(trip.getUser().getId())
                    .id(trip.getId())
                    .tripName(trip.getTripName())
                    .tripStartDate(trip.getTripStartDate())
                    .tripEndDate(trip.getTripEndDate())
                    .tripType(trip.getTripType())
                    .itineraryNames(itineraryNames)
                    .build();

            tripListResponse.add(tripResponse);
        }

        return tripListResponse;
    }

    @Transactional
    public TripResponse getTripById(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(
                () -> new TripNotFoundException(NO_TRIP.getMessage())
        );

        return TripResponse.fromEntity(trip);
    }

    @Transactional
    public TripUpdateResponse editTripById(Long tripId, TripUpdateRequest tripUpdateRequest) {

        Integer num = tripRepository.countTripByIdAndTripName(tripId, tripUpdateRequest.getTripName());
        validateTripUpdateRequest(tripId, tripUpdateRequest);
        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new TripNotFoundException(NO_TRIP.getMessage())
        );

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long tripUserId = trip.getUser().getId();

        checkTripAuthorization(currentUserId, tripUserId);

        Integer numberOfItinerary = getNumberOfItinerary(tripId);
        trip.update(tripUpdateRequest);
        return TripUpdateResponse.fromEntity(trip, numberOfItinerary);
    }

    @Transactional
    public String deleteTrip(Long id) {
        Trip trip = validateTripEmpty(id);

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long tripUserId = trip.getUser().getId();

        checkTripAuthorization(currentUserId, tripUserId);

        tripRepository.deleteById(id);
        return id+"번 여행이 삭제되었습니다.";
    }

    private void checkTripAuthorization(Long currentUserId, Long tripUserId) {
        if (!currentUserId.equals(tripUserId)) {
            throw new RuntimeException("해당 여행을 조작할 권한이 없습니다.");
        }
    }

    private void validateTripUpdateRequest(Long tripId, TripUpdateRequest tripUpdateRequest) {
        // 여행 시작날짜 종료날짜 범위 체크 --> 종료날짜가 시작날짜보다 빠를 수 없음.
        if (tripUpdateRequest.getTripStartDate().isAfter(tripUpdateRequest.getTripEndDate())) {
            throw new InvalidTripScheduleException();
        }
        //해당 tripid로 조회한 tripName이 아니면서 동시에
        if (tripRepository.existsByTripName(tripUpdateRequest.getTripName())
                && tripRepository.countTripByIdAndTripName(tripId, tripUpdateRequest.getTripName()) != 1) {
            throw new DuplicateTripNameException(DUPLICATE_TRIP_NAME.getMessage());
        }
    }

    public Integer getNumberOfItinerary(Long tripId) {
        List<Itinerary> itinerary = itineraryRepository.findItinerariesByTripId(tripId).orElseThrow(
                () -> new TripNotFoundException(NO_ITINERARY.getMessage())
        );
        return itinerary.size();
    }

    private Trip validateTripEmpty(Long id) {
        return tripRepository.findById(id).orElseThrow(
                () -> new NullTripListException()
        );
    }
}


