package com.colorpl.reservation.status.service;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisableReservationService {

    private final GetReservationStatusService getReservationStatusService;
    private final ReservationStatusRepository reservationStatusRepository;

    @Value("${time.to.live}")
    private Long expiration;

    public void disableReservation(Long showScheduleId, Integer row, Integer col) {
        ReservationStatus reservationStatus = getReservationStatusService.getReservationStatusByShowScheduleIdWithCaching(
            showScheduleId);
        reservationStatus.disableReservation(row, col, expiration);
        reservationStatusRepository.save(reservationStatus);
    }
}
