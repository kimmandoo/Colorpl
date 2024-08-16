package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowDetailRepository extends JpaRepository<ShowDetail, Integer>,
    ShowDetailRepositoryCustom {

    boolean existsByApiId(String apiId);
}
