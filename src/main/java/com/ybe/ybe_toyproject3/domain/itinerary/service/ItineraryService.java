package com.ybe.ybe_toyproject3.domain.itinerary.service;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryCreateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryUpdateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryUpdateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.exception.InvalidItineraryScheduleException;
import com.ybe.ybe_toyproject3.domain.itinerary.exception.ItineraryNotFoundException;
import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ybe.ybe_toyproject3.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;

    @Transactional
    public ItineraryCreateResponse createItinerary(Long tripId, ItineraryCreateRequest itineraryCreateRequest) {

        Trip trip = validateTrip(tripId);

        validateItineraryTimeRange(itineraryCreateRequest, trip);
        validateItineraryCreateRequest(itineraryCreateRequest);

        Itinerary itinerary = itineraryCreateRequest.toEntity();
        itinerary.add(trip);

        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        return ItineraryCreateResponse.fromEntity(savedItinerary);
    }

    @Transactional
    public ItineraryUpdateResponse editItinerary(
            Long itineraryId, ItineraryUpdateRequest request, Long tripId
    ) {
        Itinerary itinerary = validateItinerary(itineraryId, tripId);
        validateItineraryUpdateRequest(request);

        itinerary.update(request);

        return ItineraryUpdateResponse.fromEntity(itinerary);
    }

    @Transactional
    public String deleteItinerary(Long itineraryId, Long tripId) {
        validateItinerary(itineraryId, tripId);

        Itinerary itinerary = itineraryRepository.findById(itineraryId).get();

        itineraryRepository.delete(itinerary);

        return itineraryId+"번 여정이 삭제되었습니다.";
    }

    private Trip validateTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(
                        () -> new TripNotFoundException(NO_TRIP.getMessage())
                );
    }

    private Itinerary validateItinerary(Long itineraryId, Long TripId) {
        if (!tripRepository.existsById(TripId)){
            throw new TripNotFoundException();
        }
        return itineraryRepository.findById(itineraryId)
                .orElseThrow(
                        () -> new ItineraryNotFoundException(EMPTY_ITINERARY.getMessage())
                );
    }

    private void validateItineraryUpdateRequest(ItineraryUpdateRequest request) {
        validateItineraryRequest(request.getCityDepartTime(), request.getCityArriveTime(), request.getCheckInTime(), request.getCheckOutTime(), request.getPlaceArriveTime(), request.getPlaceDepartTime());
    }

    private void validateItineraryCreateRequest(ItineraryCreateRequest request) {
        validateItineraryRequest(request.getCityDepartTime(), request.getCityArriveTime(), request.getCheckInTime(), request.getCheckOutTime(), request.getPlaceArriveTime(), request.getPlaceDepartTime());
    }

    private void validateItineraryRequest(LocalDateTime cityDepartTime, LocalDateTime cityArriveTime, LocalDateTime checkInTime, LocalDateTime checkOutTime, LocalDateTime placeArriveTime, LocalDateTime placeDepartTime) {
        //도시 시간 관련
        if (cityDepartTime.isAfter(cityArriveTime) ||
                cityDepartTime.isEqual(cityArriveTime)) {
            throw new InvalidItineraryScheduleException(INVALID_ITINERARY_SCHEDULE.getMessage());
        }
        //숙소 시간 관련
        if (checkInTime.isAfter(checkOutTime) ||
                checkInTime.isEqual(checkOutTime)) {
            throw new InvalidItineraryScheduleException(INVALID_ACCOMMODATION_SCHEDULE.getMessage());
        }
        //장소 시간 관련: 장소 출발 <
        if (placeDepartTime.isAfter(placeArriveTime) ||
                placeDepartTime.isEqual(placeArriveTime)) {
            throw new InvalidItineraryScheduleException(INVALID_PLACE_SCHEDULE.getMessage());
        }
        //도시와 장소, 숙소 관련 시간 범위
        if (placeDepartTime.isBefore(cityArriveTime)) {
            throw new InvalidItineraryScheduleException(INVALID_PLACE_ARRIVAL.getMessage());
        }
        if (checkInTime.isBefore(cityArriveTime)) {
            throw new InvalidItineraryScheduleException(INVALID_ACCOMMODATION_ARRIVAL.getMessage());
        }
    }

    private void validateItineraryTimeRange(ItineraryCreateRequest request, Trip trip) {
        if (request.getCityDepartTime().isBefore(trip.getTripStartDate()) ||
                request.getCityArriveTime().isAfter(trip.getTripEndDate())) {
            throw new InvalidItineraryScheduleException(INVALID_ITINERARY_TIME_RANGE.getMessage());
        }
        if (request.getCheckOutTime().isAfter(trip.getTripEndDate())) {
            throw new InvalidItineraryScheduleException(INVALID_ITINERARY_TIME_RANGE.getMessage());
        }
        if (request.getPlaceArriveTime().isAfter(trip.getTripEndDate())) {
            throw new InvalidItineraryScheduleException(INVALID_ITINERARY_TIME_RANGE.getMessage());
        }
    }
}