package com.colorpl.schedule.repository;

import com.colorpl.reservation.domain.Reservation;
import com.colorpl.schedule.domain.ReservationSchedule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule, Long>,
    ReservationScheduleRepositoryCustom {

    Optional<ReservationSchedule> findByReservation(Reservation reservation);
}
