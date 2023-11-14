package com.ybe.ybe_toyproject3.domain.trip.dto.response;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.comment.dto.CommentReadResponse;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripListResponse {
    @Schema(description = "사용자 ID", defaultValue = "1")
    private Long userId;
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
    @Schema(description = "좋아요 갯수", defaultValue = "0")
    private Integer likesCount;
    @Schema(description = "댓글 리스트", defaultValue = "작성된 댓글 리스트")
    private List<CommentReadResponse> commentReadResponseList = new ArrayList<>();

    public static TripListResponse fromEntity(Trip trip) {
        List<CommentReadResponse> commentReadResponseList = trip.getCommentList()
                .stream()
                .map(CommentReadResponse::fromEntity)
                .collect(Collectors.toList());

        return TripListResponse.builder()
                .userId(trip.getUser().getId())
                .id(trip.getId())
                .tripName(trip.getTripName())
                .tripStartDate(trip.getTripStartDate())
                .tripEndDate(trip.getTripEndDate())
                .tripType(trip.getTripType())
                .likesCount(trip.getLikesList().size())
                .commentReadResponseList(commentReadResponseList)
                .build();
    }

}

