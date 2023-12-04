package com.ybe.ybe_toyproject3.domain.trip.repository;

import com.ybe.ybe_toyproject3.domain.trip.dto.response.TripResponse;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Transactional
    @Test
    void findTripByTripNameContaining() {
        Optional<Trip> trip = tripRepository.findTripByTripNameContaining("야놀자");
        System.out.println(TripResponse.fromEntity(trip.get()).toString());
    }

    @Test
    @Transactional
    void findAll() {
        List<Trip> tripList = tripRepository.findAll();
        for (Trip trip : tripList) {
            System.out.println(trip.getCommentList().size());
            System.out.println(trip.getLikesList().size());
        }
    }
}