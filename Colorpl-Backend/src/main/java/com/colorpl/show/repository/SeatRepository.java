package com.colorpl.show.repository;

import com.colorpl.show.domain.Seat;
import com.colorpl.show.domain.ShowDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowDetail(ShowDetail showDetail);
}
