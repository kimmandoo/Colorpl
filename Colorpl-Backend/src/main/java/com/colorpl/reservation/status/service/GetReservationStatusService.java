package com.colorpl.reservation.status.service;

import com.colorpl.global.common.exception.ShowScheduleNotFoundException;
import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetReservationStatusService {

    private final ReservationStatusRepository reservationStatusRepository;

    public ReservationStatus getReservationStatus(Long showScheduleId) {
        return reservationStatusRepository.findById(showScheduleId)
            .orElseThrow(ShowScheduleNotFoundException::new);
    }
}
