package com.ybe.ybe_toyproject3.domain.itinerary.model;

import com.ybe.ybe_toyproject3.domain.location.model.Location;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itinerary_id")
    private Long id;
    private String itineraryName;
    private String transportation;
    private String departCity;
    private String arriveCity;
    private LocalDateTime cityDepartTime;
    private LocalDateTime cityArriveTime;
    private String accommodation;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String placeName;
    private LocalDateTime placeArriveTime;
    private LocalDateTime placeDepartTime;

    // @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private Location location;

    @Builder
    public Itinerary(String itineraryName, String transportation, String departCity, String arriveCity, LocalDateTime cityDepartTime, LocalDateTime cityArriveTime, String accommodation, LocalDateTime checkInTime, LocalDateTime checkOutTime, String placeName, LocalDateTime placeArriveTime, LocalDateTime placeDepartTime, Location location) {
        this.itineraryName = itineraryName;
        this.transportation = transportation;
        this.departCity = departCity;
        this.arriveCity = arriveCity;
        this.cityDepartTime = cityDepartTime;
        this.cityArriveTime = cityArriveTime;
        this.accommodation = accommodation;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.placeName = placeName;
        this.placeDepartTime = placeDepartTime;
        this.placeArriveTime = placeArriveTime;
        this.location = location;
    }

    public void add(Trip trip) {
        this.trip = trip;
    }

}