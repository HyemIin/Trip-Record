package com.ybe.ybe_toyproject3.domain.location.model;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;
    private String locationName;

    @OneToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    public Location(String locationName) {
        this.locationName = locationName;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public void updateLocationName(String locationName) {
        this.locationName = locationName;
    }
}