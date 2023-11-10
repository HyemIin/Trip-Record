package com.ybe.ybe_toyproject3.domain.itinerary.controller;

import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryCreateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.request.ItineraryUpdateRequest;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryCreateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.dto.response.ItineraryUpdateResponse;
import com.ybe.ybe_toyproject3.domain.itinerary.service.ItineraryService;
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

@Tag(name = "여정 API", description = "여정 관련 API 모음입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Operation(summary = "여정 생성 API", description = "여정 생성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공시",
            content = @Content(schema = @Schema(implementation = ItineraryCreateResponse.class)))
    @FailApiResponses
    @PostMapping("/{tripId}")
    public ResponseEntity<ItineraryCreateResponse> create(
            @PathVariable Long tripId,
            @Valid @RequestBody ItineraryCreateRequest request
    ) {
        ItineraryCreateResponse response = itineraryService.createItinerary(tripId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "여행 수정 API", description = "여행 수정 API 입니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공시", content = @Content(schema = @Schema(implementation = ItineraryUpdateResponse.class)))
    @FailApiResponses
    @PutMapping("/{itineraryId}")
    public ResponseEntity<ItineraryUpdateResponse> updateItinerary(
            @PathVariable Long itineraryId,
            @Valid @RequestBody ItineraryUpdateRequest request
    ) {
        return ResponseEntity.ok(
                itineraryService.editItinerary(itineraryId, request)
        );
    }

    @Operation(summary = "여정 삭제 API", description = "여정 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공 시", content = @Content(schema = @Schema(implementation = Long.class)))
    @DeleteMapping("/{itineraryId}")
    public ResponseEntity<String> deleteItinerary(
            @PathVariable Long itineraryId
    ) {
        String deletedItineraryId = itineraryService.deleteItinerary(itineraryId);
        return ResponseEntity.ok(
                deletedItineraryId
        );
    }
}
