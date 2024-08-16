package com.colorpl.reservation.status.service;

import com.colorpl.reservation.status.repository.ReservationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAllReservationStatusService {

    private final ReservationStatusRepository reservationStatusRepository;

    public void deleteAllReservationStatus() {
        reservationStatusRepository.deleteAll();
    }
}
