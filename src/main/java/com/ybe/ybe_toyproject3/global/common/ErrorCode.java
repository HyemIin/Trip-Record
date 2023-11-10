package com.ybe.ybe_toyproject3.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATE_TRIP_NAME("중복되는 이름이 존재합니다."),

    NO_TRIP("해당되는 여행이 없습니다."),
    NO_ITINERARY("해당되는 여정이 없습니다."),

    EMPTY_ITINERARY("해당 여정id는 이미 삭제되었거나 존재하지 않습니다."),

    INVALID_ITINERARY_TIME_RANGE("여정 시간 범위는 여행 시간 범위를 넘어갈 수 없습니다."),
    INVALID_ITINERARY_SCHEDULE("도시의 출발 일시는 도착 일시보다 이전이어야 합니다."),
    INVALID_ACCOMMODATION_SCHEDULE("숙소의 체크인 일시는 체크아웃 일시보다 이전이어야 합니다."),
    INVALID_PLACE_SCHEDULE("장소의 출발 일시는 도착 일시 이전이어야 합니다."),
    INVALID_PLACE_ARRIVAL("장소의 출발 일시는 도시의 도착 일시 이후여야 합니다."),
    INVALID_ACCOMMODATION_ARRIVAL("숙소의 체크인 일시는 도시의 도착 일시 이후여야 합니다"),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
