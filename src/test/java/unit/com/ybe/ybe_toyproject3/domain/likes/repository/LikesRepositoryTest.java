package com.ybe.ybe_toyproject3.domain.likes.repository;

import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import com.ybe.ybe_toyproject3.domain.trip.model.Trip;
import com.ybe.ybe_toyproject3.domain.trip.repository.TripRepository;
import com.ybe.ybe_toyproject3.domain.user.model.User;
import com.ybe.ybe_toyproject3.domain.user.repository.UserRepository;
import com.ybe.ybe_toyproject3.global.common.Authority;
import com.ybe.ybe_toyproject3.global.common.type.TripType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;

    private static User user;
    private static Trip trip;
    @BeforeEach
    void setUp() {
        user = new User("test1@mail.com", "test1", "1234", Authority.ROLE_USER);
        trip = new Trip("trip1",
                LocalDateTime.of(2024,11,12,12,0),
                LocalDateTime.of(2024,11,12,12,0),
                TripType.DOMESTIC);
    }

    @Test
    @Transactional
    void saveLikes() {
        Likes likes = new Likes(trip, user);
        Likes saved = likesRepository.save(likes);

        assertThat(saved.getTrip()).isEqualTo(trip);
        assertThat(saved.getUser()).isEqualTo(user);
    }

    @Test
    @Transactional
    void findByTripIdAndUserId() {
        Trip savedTrip = tripRepository.save(trip);
        User savedUser = userRepository.save(user);

        Likes likes = new Likes(savedTrip,savedUser);
        Likes saved = likesRepository.save(likes);

        Long tripId = savedTrip.getId();
        Long userId = savedUser.getId();
        Likes foundLikes = likesRepository.findByTripIdAndUserId(tripId,userId)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(foundLikes).isEqualTo(saved);
    }

    @Test
    @Transactional
    void deleteLikes() {
        Trip savedTrip = tripRepository.save(trip);
        User savedUser = userRepository.save(user);

        Likes likes = new Likes(savedTrip,savedUser);
        likesRepository.save(likes);

        Long tripId = savedTrip.getId();
        Long userId = savedUser.getId();
        Likes foundLikes = likesRepository.findByTripIdAndUserId(tripId,userId)
                .orElseThrow(IllegalArgumentException::new);

        Long foundLikesId = foundLikes.getId();
        likesRepository.deleteById(foundLikesId);

        Optional<Likes> likesFoundById = likesRepository.findById(foundLikesId);
        assertThat(likesFoundById).isNotPresent();
    }

}