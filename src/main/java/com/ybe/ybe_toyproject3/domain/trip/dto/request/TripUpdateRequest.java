package com.ybe.ybe_toyproject3.domain.trip.dto.request;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripUpdateRequest {
    @NotBlank //null이 아니어야 하고 최소 하나의 문자 있어야 함.
    private String tripName;
    @NotNull
    private LocalDateTime tripStartDate;
    @NotNull
    private LocalDateTime tripEndDate;
    @NotNull
    private TripType tripType;

    public static Trip toEntity(TripUpdateRequest request) {
        return Trip.builder()
                .tripName(request.tripName)
                .tripStartDate(request.tripStartDate)
                .tripEndDate(request.tripEndDate)
                .tripType(request.tripType)
                .build();
    }

}
