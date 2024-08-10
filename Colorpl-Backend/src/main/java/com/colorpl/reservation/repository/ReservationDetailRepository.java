package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {


//    @Query("SELECT COUNT(rd) FROM ReservationDetail rd WHERE rd.showSchedule = :showSchedule")
//    long countByShowSchedule(@Param("showSchedule") ShowSchedule showSchedule);
    long countByShowScheduleId(Long showScheduleId);
}
