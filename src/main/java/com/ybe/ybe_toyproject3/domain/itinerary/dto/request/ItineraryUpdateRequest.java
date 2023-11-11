package com.ybe.ybe_toyproject3.domain.itinerary.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryUpdateRequest {
    @Schema(description = "여정 이름", defaultValue = "수정할 여정 이름")
    private String itineraryName;
    @Schema(description = "이동 수단", defaultValue = "수정할 이동 수단")
    private String transportation;
    @Schema(description = "출발지", defaultValue = "수정할 출발지")
    private String departCity;
    @Schema(description = "도착지", defaultValue = "수정할 도착지")
    private String arriveCity;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cityDepartTime;
    @Schema(description = "도시 일시", defaultValue = "2023-10-25T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cityArriveTime;
    @Schema(description = "숙소명", defaultValue = "수정할 숙소명")
    private String accommodation;
    @Schema(description = "체크인 시간", defaultValue = "2023-10-25T14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkInTime;
    @Schema(description = "체크아웃 시간", defaultValue = "2023-10-26T12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOutTime;
    @Schema(description = "장소명", defaultValue = "수정할 장소명")
    private String placeName;
    @Schema(description = "도착 일시", defaultValue = "2023-10-25T13:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime placeArriveTime;
    @Schema(description = "출발 일시", defaultValue = "2023-10-25T14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime placeDepartTime;
}