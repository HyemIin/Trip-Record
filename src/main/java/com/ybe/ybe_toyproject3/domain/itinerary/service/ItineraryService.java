package com.ybe.ybe_toyproject3.domain.itinerary.service;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryCreateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryUpdateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryUpdateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.exception.InvalidItineraryScheduleException;
import com.ybe.ybe_toyproject3.domain.itinerary.exception.ItineraryNotFoundException;
import com.ybe.ybe_toyproject3.domain.itinerary.exception.LocationNameNotFoundException;
import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import com.ybe.ybe_toyproject3.domain.itinerary.repository.ItineraryRepository;
import com.ybe.ybe_toyproject3.domain.location.model.Location;
import com.ybe.ybe_toyproject3.domain.location.repository.LocationRepository;
import com.ybe.ybe_toyproject3.domain.trip.exception.TripNotFoundException;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.global.component.KakaoApiComponent;

import com.ybe.ybe_toyproject3.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ybe.ybe_toyproject3.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final LocationRepository locationRepository;

    private final KakaoApiComponent kakaoApiComponent;

    @Transactional
    public ItineraryCreateResponse createItinerary(Long tripId, ItineraryCreateRequest itineraryCreateRequest)  {
        Trip trip = validateTrip(tripId);

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long tripUserId = trip.getUser().getId();

        checkItineraryAuthorization(currentUserId, tripUserId);

        String locationName = createLocation(itineraryCreateRequest.getPlaceName());
        Location location = new Location(locationName);

        Trip trip = validateTrip(tripId);

        validateItineraryTimeRange(itineraryCreateRequest, trip);
        validateItineraryCreateRequest(itineraryCreateRequest);

        Itinerary itinerary = itineraryCreateRequest.toEntity();
        itinerary.add(trip);
        itinerary.setLocation(location);

        location.setItinerary(itinerary);
        locationRepository.save(location);

        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        return ItineraryCreateResponse.fromEntity(savedItinerary);
    }

    public String createLocation(String placeName) {
        String locationName = kakaoApiComponent.getLocationNameByPlaceName(placeName);

        if (locationName == null) {
            throw new LocationNameNotFoundException(NO_LOCATION_NAME.getMessage());
        }

        return locationName;
    }

    @Transactional
    public ItineraryUpdateResponse editItinerary(
            Long itineraryId, ItineraryUpdateRequest request,
            Long tripId
    ) {
        Itinerary itinerary = validateItinerary(itineraryId);

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long tripUserId = itinerary.getTrip().getUser().getId();

        checkItineraryAuthorization(currentUserId, tripUserId);

        String locationName = createLocation(request.getPlaceName());

        Itinerary itinerary = validateItinerary(itineraryId);
        validateItineraryUpdateRequest(request);

        Location location = itinerary.getLocation();
        location.updateLocationName(locationName);

        itinerary.update(request);

        return ItineraryUpdateResponse.fromEntity(itinerary);
    }

    @Transactional
    public String deleteItinerary(Long itineraryId) {
        Itinerary itinerary = validateItinerary(itineraryId);

        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long tripUserId = itinerary.getTrip().getUser().getId();

        checkItineraryAuthorization(currentUserId, tripUserId);

        itineraryRepository.delete(itinerary);

        return itineraryId+"번 여정이 삭제되었습니다.";
    }

    private void checkItineraryAuthorization(Long currentUserId, Long tripUserId) {
        if (!currentUserId.equals(tripUserId)) {
            throw new RuntimeException("해당 여행에 대한 여정을 조작할 수 있는 권한이 없습니다.");
        }
    }

    private Trip validateTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(
                        () -> new TripNotFoundException(NO_TRIP.getMessage())
                );
    }

    private Itinerary validateItinerary(Long itineraryId) {
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

    public List<ItineraryResponse> getItinerary(Long tripId, String searchCondition) {
        validateTrip(tripId);
        if (searchCondition != null) {
            return toItinerarayResponseList(itineraryRepository.findItinerariesByPlaceNameContaining(searchCondition));
        }
        return toItinerarayResponseList(itineraryRepository.findItinerariesByTripId(tripId));
    }

    private List<ItineraryResponse> toItinerarayResponseList(List<Itinerary> itineraries) {
        List<ItineraryResponse> itineraryResponseList = new ArrayList<>();
        for (var it : itineraries) {
            itineraryResponseList.add(ItineraryResponse.fromEntity(it));
        }
        return itineraryResponseList;
    }
}
