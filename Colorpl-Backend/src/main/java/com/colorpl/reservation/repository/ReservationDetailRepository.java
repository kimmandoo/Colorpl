package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.ShowSchedule;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {

    List<ReservationDetail> findByShowSchedule(ShowSchedule showSchedule);
    long countByShowScheduleId(Long showScheduleId);
    List<ReservationDetail> findByShowScheduleId(Long showScheduleId);


    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rd FROM ReservationDetail rd WHERE rd.showSchedule.id = :showScheduleId AND rd.row = :row AND rd.col = :col")
    ReservationDetail findByShowScheduleIdAndRowAndColForUpdate(Long showScheduleId, Byte row, Byte col);
}
