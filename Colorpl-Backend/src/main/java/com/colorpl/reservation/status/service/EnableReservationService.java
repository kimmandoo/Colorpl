package com.colorpl.reservation.status.service;

import com.colorpl.global.common.exception.ReservationStatusAlreadyEnabledException;
import com.colorpl.global.common.exception.ShowScheduleNotFoundException;
import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import com.colorpl.show.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnableReservationService {

    private final ReservationStatusRepository reservationStatusRepository;

    public void enableReservation(Long showScheduleId, Integer row, Integer col) {
        ReservationStatus reservationStatus = reservationStatusRepository.findById(showScheduleId)
            .orElseThrow(ShowScheduleNotFoundException::new);
        Seat seat = Seat.builder()
            .row(row)
            .col(col)
            .build();
        if (reservationStatus.getReserved().get(seat.toString())) {
            throw new ReservationStatusAlreadyEnabledException(showScheduleId, row, col);
        }
        reservationStatus.getReserved().put(seat.toString(), true);
        reservationStatusRepository.save(reservationStatus);
    }
}
