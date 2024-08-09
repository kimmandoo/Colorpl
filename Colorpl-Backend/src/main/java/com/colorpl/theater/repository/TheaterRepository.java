package com.colorpl.theater.repository;

import com.colorpl.theater.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Integer> {

    Boolean existsByApiId(String apiId);
}
