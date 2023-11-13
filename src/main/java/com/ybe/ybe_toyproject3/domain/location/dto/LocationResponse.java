package com.ybe.ybe_toyproject3.domain.location.dto;

import com.ybe.ybe_toyproject3.domain.location.model.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {
    private Long locationId;
    private String locationName;

    public static LocationResponse fromEntity(Location location) {
        return LocationResponse.builder()
                .locationId(location.getId())
                .locationName(location.getLocationName())
                .build();
    }
}