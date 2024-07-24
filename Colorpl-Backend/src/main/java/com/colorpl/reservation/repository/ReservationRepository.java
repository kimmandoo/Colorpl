package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //유저로 조회 추가


}
