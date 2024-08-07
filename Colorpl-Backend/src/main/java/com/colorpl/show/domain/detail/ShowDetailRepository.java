package com.colorpl.show.domain.detail;

import com.colorpl.show.query.dao.ShowDetailRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowDetailRepository extends JpaRepository<ShowDetail, Long>,
    ShowDetailRepositoryCustom {

    boolean existsByApiId(String apiId);

    Optional<ShowDetail> findByApiId(String apiId);
}
