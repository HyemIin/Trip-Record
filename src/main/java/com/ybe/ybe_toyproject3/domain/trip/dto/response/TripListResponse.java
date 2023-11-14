package com.ybe.ybe_toyproject3.domain.trip.dto.response;

import com.ybe.ybe_toyproject3.domain.comment.dto.CommentReadResponse;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripListResponse {
    @Schema(description = "여행 ID", defaultValue = "1")
    private Long id;
    @Schema(description = "여행 이름", defaultValue = "조회된 여행 이름")
    private String tripName;
    @Schema(description = "여행 시작 일자", defaultValue = "2023-10-23T12:00:00")
    private LocalDateTime tripStartDate;
    @Schema(description = "여행 종료 일자", defaultValue = "2023-10-30T12:00:00")
    private LocalDateTime tripEndDate;
    @Schema(description = "여행 타입", defaultValue = "조회된 여행 타입")
    private TripType tripType;

    private List<CommentReadResponse> commentReadResponseList = new ArrayList<>();
    //@ArraySchema(schema = @Schema(description = "조회된 여정 이름 목록"))
    //private List<String> itineraryNames;
}

