package com.colorpl.theater.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    Optional<Theater> findByApiId(String apiId);
}
