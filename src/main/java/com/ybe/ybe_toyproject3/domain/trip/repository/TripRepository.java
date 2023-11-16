package com.ybe.ybe_toyproject3.domain.trip.repository;

import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select t from Trip t join fetch t.user  left join fetch t.commentList")
    List<Trip> findAll();
    Optional<Trip> findById(Long tripId);

    Boolean existsByTripName(String tripName);

    Integer countTripByIdAndTripName(Long tripId, String tripName);

    List<Trip> findAllByTripNameContaining(String tripName);

    Optional<Trip> findTripByTripNameContaining(String tripName);
}
