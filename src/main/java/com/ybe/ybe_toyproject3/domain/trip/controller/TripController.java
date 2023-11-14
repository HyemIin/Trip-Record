package com.ybe.ybe_toyproject3.domain.trip.controller;

import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripCreateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.request.TripUpdateRequest;
import com.ybe.ybe_toyproject3.domain.trip.dto.response.*;
import com.ybe.ybe_toyproject3.domain.trip.service.TripService;
import com.ybe.ybe_toyproject3.global.common.annotation.FailApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "여행 API", description = "여행 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    @Operation(summary = "여행 생성 API", description = "여행 생성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = TripCreateResponse.class)))
    @FailApiResponses
    @PostMapping("")
    public ResponseEntity<TripCreateResponse> createTrip(@Valid @RequestBody TripCreateRequest tripCreateRequest) {
        return ResponseEntity.ok(tripService.createTrip(tripCreateRequest));
    }

    @Operation(summary = "전체 여행 조회 API", description = "전체 여행을 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공 시", content = @Content(schema = @Schema(implementation = TripListResponse.class)))
    @FailApiResponses
    @GetMapping("")
    public ResponseEntity<List<TripListResponse>> findAllTrips(
            @RequestParam(value = "search-condition", required = false) String searchCondition
    ) {
        return ResponseEntity.ok(
                tripService.findAllTrips(searchCondition)
        );
    }

    @Operation(summary = "특정 여행 조회 API", description = "특정 여행을 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공 시", content = @Content(schema = @Schema(implementation = TripResponse.class)))
    @FailApiResponses
    @GetMapping("/{tripId}")
    public ResponseEntity<TripDetailResponse> getTripById(
            @PathVariable Long tripId
    ) {
        return ResponseEntity.ok(
                tripService.getTripById(tripId)
        );
    }

    @Operation(summary = "여행 수정 API", description = "특정 여행을 수정하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공 시", content = @Content(schema = @Schema(implementation = TripUpdateResponse.class)))
    @FailApiResponses
    @PutMapping("/{tripId}")
    public ResponseEntity<TripUpdateResponse> editTrip(
            @PathVariable Long tripId,
            @Valid @RequestBody TripUpdateRequest tripUpdateRequest) {
        //System.out.println("REQUEST SUCCESS");
        return ResponseEntity.ok(tripService.editTripById(tripId, tripUpdateRequest));
    }

    @Operation(summary = "여행 삭제 API", description = "여행 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공시", content = @Content(schema = @Schema(implementation = TripResponse.class)))
    @FailApiResponses
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.deleteTrip(id));
    }
}
