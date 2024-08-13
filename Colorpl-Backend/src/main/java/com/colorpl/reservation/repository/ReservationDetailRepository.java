package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.ShowSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {

    List<ReservationDetail> findByShowSchedule(ShowSchedule showSchedule);
    long countByShowScheduleId(Long showScheduleId);
    List<ReservationDetail> findByShowScheduleId(Long showScheduleId);
}
