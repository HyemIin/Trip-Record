package com.ybe.ybe_toyproject3.global.common.type;

public enum TripType {
    DOMESTIC(0, "DOMESTIC"), GLOBAL(1, "GLOBAL");

    private long id;
    private String type;

    TripType(long id, String type) {
        this.id = id;
        this.type = type;
    }
}