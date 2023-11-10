package com.ybe.ybe_toyproject3.domain.itinerary.dto.response;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryCreateResponse {

    private Long itineraryId;
    @Schema(description = "여정 이름", defaultValue = "생성된 여정 이름")
    private String itineraryName;
    @Schema(description = "이동 수단", defaultValue = "생성된 이동 수단")
    private String transportation;
    @Schema(description = "출발지", defaultValue = "생성된 출발지")
    private String departCity;
    @Schema(description = "도착지", defaultValue = "생성된 도착지")
    private String arriveCity;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T10:00:00")
    private LocalDateTime cityDepartTime;
    @Schema(description = "도시 일시", defaultValue = "2023-10-25T12:00:00")
    private LocalDateTime cityArriveTime;
    @Schema(description = "숙소명", defaultValue = "생성된 숙소명")
    private String accommodation;
    @Schema(description = "체크인 시간", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime checkInTime;
    @Schema(description = "체크아웃 시간", defaultValue = "2023-10-26T12:00:00")
    private LocalDateTime checkOutTime;
    @Schema(description = "장소명", defaultValue = "생성된 장소명")
    private String placeName;
    @Schema(description = "장소 출발 일시", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime placeDepartTime;
    @Schema(description = "장소 도착 일시", defaultValue = "2023-10-25T13:00:00")
    private LocalDateTime placeArriveTime;


    public static ItineraryCreateResponse fromEntity(Itinerary itinerary) {
        return ItineraryCreateResponse.builder()
                .itineraryId(itinerary.getId())
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
                .placeArriveTime(itinerary.getPlaceArriveTime())
                .placeDepartTime(itinerary.getPlaceDepartTime())
                .build();
    }
}
