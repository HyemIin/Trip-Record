package com.ybe.ybe_toyproject3.domain.trip.dto.request;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 생성시 요청 데이터")
public class TripCreateRequest {
    @Schema(description = "여행 명", defaultValue = "생성할 여행 이름")
    @NotNull
    String tripName;
    @Schema(description = "여행 시작 일시", defaultValue = "2023-10-23T12:00:00")
    @NotNull
    LocalDateTime tripStartDate;
    @Schema(description = "여행 종료 일시", defaultValue = "2023-10-30T13:00:00")
    @NotNull
    LocalDateTime tripEndDate;
    @Schema(description = "여행 타입", defaultValue = "DOMESTIC OR GLOBAL")
    @NotNull
    TripType tripType;

    public Trip toEntity() {
        return Trip.builder()
                .tripName(tripName)
                .tripStartDate(tripStartDate)
                .tripEndDate(tripEndDate)
                .tripType(tripType)
                .build();
    }

}
