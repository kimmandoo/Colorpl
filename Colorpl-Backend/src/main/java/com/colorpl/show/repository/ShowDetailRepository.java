package com.colorpl.show.repository;

import com.colorpl.show.domain.detail.ShowDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowDetailRepository extends JpaRepository<ShowDetail, Long>,
    ShowDetailRepositoryCustom {

    boolean existsByApiId(String apiId);
}
