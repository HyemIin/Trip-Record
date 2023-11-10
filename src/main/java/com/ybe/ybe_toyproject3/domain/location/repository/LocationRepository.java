package com.ybe.ybe_toyproject3.domain.location.repository;

import com.ybe.ybe_toyproject3.domain.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
