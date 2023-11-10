package com.ybe.ybe_toyproject3.domain.trip.repository;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

}
