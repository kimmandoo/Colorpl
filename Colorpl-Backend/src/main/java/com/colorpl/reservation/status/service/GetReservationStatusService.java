package com.colorpl.reservation.status.service;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetReservationStatusService {

    private final ReservationStatusRepository reservationStatusRepository;
    private final CreateReservationStatusService createReservationStatusService;

    public ReservationStatus getReservationStatusByShowScheduleIdWithCaching(Long showScheduleId) {
        return reservationStatusRepository.findById(showScheduleId).orElseGet(
            () -> createReservationStatusService.createReservationStatus(showScheduleId));
    }

    public ReservationStatus getReservationStatusByShowScheduleId(Long showScheduleId) {
        return createReservationStatusService.createReservationStatus(showScheduleId);
    }
}
