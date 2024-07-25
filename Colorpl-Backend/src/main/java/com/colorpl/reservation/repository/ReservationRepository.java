package com.colorpl.reservation.repository;

import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.domain.ReservationDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //유저로 조회 추가
    List<Reservation> findByMemberId(Integer memberId);
    Optional<ReservationDetail> findDetailByIdAndMemberId(Long detailId, Integer memberId);


}
