package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.ShowSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {

    List<ReservationDetail> findByShowSchedule(ShowSchedule showSchedule);
//    @Query("SELECT COUNT(rd) FROM ReservationDetail rd WHERE rd.showSchedule = :showSchedule")
//    long countByShowSchedule(@Param("showSchedule") ShowSchedule showSchedule);
}
