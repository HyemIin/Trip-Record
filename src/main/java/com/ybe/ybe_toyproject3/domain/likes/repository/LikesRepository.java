package com.ybe.ybe_toyproject3.domain.likes.repository;

import com.ybe.ybe_toyproject3.domain.likes.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

}
