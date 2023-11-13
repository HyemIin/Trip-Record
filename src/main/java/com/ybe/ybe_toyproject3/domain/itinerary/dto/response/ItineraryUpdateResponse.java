package com.ybe.ybe_toyproject3.domain.itinerary.dto.response;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryUpdateResponse {
    @Schema(description = "여정 이름", defaultValue = "수정된 여정 이름")
    private String itineraryName;
    @Schema(description = "이동 수단", defaultValue = "수정된 이동 수단")
    private String transportation;
    @Schema(description = "출발지", defaultValue = "수정된 출발지")
    private String departCity;
    @Schema(description = "도착지", defaultValue = "수정된 도착지")
    private String arriveCity;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T10:00:00")
    private LocalDateTime cityDepartTime;
    @Schema(description = "도착 일시", defaultValue = "2023-10-25T12:00:00")
    private LocalDateTime cityArriveTime;
    @Schema(description = "숙소명", defaultValue = "수정된 숙소명")
    private String accommodation;
    @Schema(description = "체크인 시간", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime checkInTime;
    @Schema(description = "체크아웃 시간", defaultValue = "2023-10-26T12:00:00")
    private LocalDateTime checkOutTime;
    @Schema(description = "장소명", defaultValue = "수정된 장소명")
    private String placeName;
    @Schema(description = "장소 도착 일시", defaultValue = "2023-10-25T13:00:00")
    private LocalDateTime placeArriveTime;
    @Schema(description = "장소 출발 일시", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime placeDepartTime;

    public static ItineraryUpdateResponse fromEntity(Itinerary itinerary) {
        return ItineraryUpdateResponse.builder()
                .itineraryName(itinerary.getItineraryName())
                .transportation(itinerary.getTransportation())
                .departCity(itinerary.getDepartCity())
                .arriveCity(itinerary.getArriveCity())
                .cityDepartTime(itinerary.getCityDepartTime())
                .cityArriveTime(itinerary.getCityArriveTime())
                .accommodation(itinerary.getAccommodation())
                .checkInTime(itinerary.getCheckInTime())
                .checkOutTime(itinerary.getCheckOutTime())
                .placeName(itinerary.getPlaceName())
                .placeDepartTime(itinerary.getPlaceDepartTime())
                .placeArriveTime(itinerary.getPlaceArriveTime())
                .build();
    }
}