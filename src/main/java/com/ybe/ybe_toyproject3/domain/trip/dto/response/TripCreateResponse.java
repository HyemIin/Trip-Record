package com.ybe.ybe_toyproject3.domain.trip.dto.response;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Schema(description = "상품 생성시 리턴 데이터")
public class TripCreateResponse {
    @Schema(description = "여행 아이디", defaultValue = "1")
    Long tripId;
    @Schema(description = "생성된 여행 이름", defaultValue = "생성된 여행 이름")
    String tripName;
    @Schema(description = "여행 시작 일시", defaultValue = "2023-10-23T12:00:00")
    LocalDateTime tripStartDate;
    @Schema(description = "여행 종료 일시", defaultValue = "2023-10-30T13:00:00")
    LocalDateTime tripEndDate;
    @Schema(description = "여행 타입", defaultValue = "DOMESTIC OR GLOBAL")
    TripType tripType;
    @Builder
    public TripCreateResponse(Long tripId, String tripName, LocalDateTime tripStartDate, LocalDateTime tripEndDate, TripType tripType) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.tripType = tripType;
    }

    public static TripCreateResponse getTripCreateResponse(Trip trip) {
        return TripCreateResponse.builder()
                .tripId(trip.getId())
                .tripName(trip.getTripName())
                .tripStartDate(trip.getTripStartDate())
                .tripEndDate(trip.getTripEndDate())
                .tripType(trip.getTripType())
                .build();
    }
}
