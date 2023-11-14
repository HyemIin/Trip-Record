package com.ybe.ybe_toyproject3.domain.likes.dto;

import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "좋아요 생성시 리턴 데이터")
public class TripLikesCreateResponse {
    @Schema(description = "좋아요 id", defaultValue = "1")
    private Long id;
    @Schema(description = "여행 id", defaultValue = "1")
    private Long tripId;
    @Schema(description = "사용자 id", defaultValue = "1")
    private Long userId;

    @Builder
    public TripLikesCreateResponse(Long id, Long tripId, Long userId) {
        this.id = id;
        this.tripId = tripId;
        this.userId = userId;
    }

    public static TripLikesCreateResponse getTripLikesCreateResponse(Likes likes) {
        return TripLikesCreateResponse.builder()
                .id(likes.getId())
                .tripId(likes.getTrip().getId())
                .userId(likes.getUser().getId())
                .build();
    }
}
