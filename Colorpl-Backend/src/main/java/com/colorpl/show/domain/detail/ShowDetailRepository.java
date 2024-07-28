package com.colorpl.show.domain.detail;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowDetailRepository extends JpaRepository<ShowDetail, Integer> {

    Optional<ShowDetail> findByApiId(String apiId);
}
