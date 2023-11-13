package com.ybe.ybe_toyproject3.domain.likes.repository;

import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByTripIdAndUserId(Long tripId, Long userId);
}
