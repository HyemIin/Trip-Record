package com.ybe.ybe_toyproject3.domain.itinerary.repository;

import com.ybe.ybe_toyproject3.domain.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}
