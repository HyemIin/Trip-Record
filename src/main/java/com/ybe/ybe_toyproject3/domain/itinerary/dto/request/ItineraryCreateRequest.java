package com.ybe.ybe_toyproject3.domain.itinerary.dto.request;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryCreateRequest {
    @Schema(description = "여정 명", defaultValue = "생성할 여정 이름")
    @NotBlank
    private String itineraryName;
    @Schema(description = "이동 수단", defaultValue = "생성할 이동수단")
    @NotBlank
    private String transportation;
    @Schema(description = "출발지", defaultValue = "생성할 출발지")
    @NotBlank
    private String departCity;
    @Schema(description = "도착지", defaultValue = "생성할 도착지")
    @NotBlank
    private String arriveCity;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T10:00:00")
    @NotNull
    private LocalDateTime cityDepartTime;
    @Schema(description = "도착 일시", defaultValue = "2023-10-25T12:00:00")
    @NotNull
    private LocalDateTime cityArriveTime;

    @Schema(description = "숙소명", defaultValue = "생성할 숙소명")
    private String accommodation;
    @Schema(description = "체크인 시간", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime checkInTime;
    @Schema(description = "체크아웃 시간", defaultValue = "2023-10-26T12:00:00")
    private LocalDateTime checkOutTime;
    @Schema(description = "장소명", defaultValue = "생성할 장소명")
    private String placeName;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T14:00:00")
    private LocalDateTime placeDepartTime;
    @Schema(description = "도착 일시", defaultValue = "2023-10-25T13:00:00")
    private LocalDateTime placeArriveTime;

    public Itinerary toEntity() {
        return Itinerary.builder()
                .itineraryName(itineraryName)
                .transportation(transportation)
                .departCity(departCity)
                .arriveCity(arriveCity)
                .cityDepartTime(cityDepartTime)
                .cityArriveTime(cityArriveTime)
                .accommodation(accommodation)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .placeName(placeName)
                .placeArriveTime(placeArriveTime)
                .placeDepartTime(placeDepartTime)
                .build();
    }
}
