package com.ybe.ybe_toyproject3.domain.user.repository;

import com.ybe.ybe_toyproject3.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);


}
