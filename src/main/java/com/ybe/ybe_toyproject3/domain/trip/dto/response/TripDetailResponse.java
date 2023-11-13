package com.ybe.ybe_toyproject3.domain.trip.dto.response;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TripDetailResponse {
    @Schema(description = "여행 ID", defaultValue = "1")
    private Long id;
    @Schema(description = "여행 이름", defaultValue = "조회된 여행 이름")
    private String tripName;
    @Schema(description = "여행 시작 일자", defaultValue = "2023-10-26T00:00:00")
    private LocalDateTime tripStartDate;
    @Schema(description = "여행 종료 일자", defaultValue = "2023-10-30T00:00:00")
    private LocalDateTime tripEndDate;
    @Schema(description = "여행 타입", defaultValue = "조회된 여행 타입")
    private TripType tripType;

    @ArraySchema(schema = @Schema(implementation = ItineraryResponse.class))
    @Schema(description = "조회된 여행에 포함된 여정 목록")
    private List<ItineraryResponse> itineraryList;

    public static TripDetailResponse fromEntity(Trip trip) {
        List<ItineraryResponse> itineraryResponse = trip.getItineraryList()
                .stream()
                .map(ItineraryResponse::fromEntity)
                .toList();

        return TripDetailResponse.builder()
                .id(trip.getId())
                .tripName(trip.getTripName())
                .tripStartDate(trip.getTripStartDate())
                .tripEndDate(trip.getTripEndDate())
                .tripType(trip.getTripType())
                .itineraryList(itineraryResponse)
                .build();
    }
}