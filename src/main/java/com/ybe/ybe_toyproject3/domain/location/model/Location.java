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

    @OneToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;
    /*

     */
}
