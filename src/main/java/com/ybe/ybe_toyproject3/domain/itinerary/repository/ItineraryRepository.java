package com.ybe.ybe_toyproject3.domain.itinerary.repository;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findItinerariesByTripId(Long tripId);

    List<Itinerary> findItinerariesByPlaceNameContaining(String placeName);
    Optional<Itinerary> findById(Long itineraryId);


}
