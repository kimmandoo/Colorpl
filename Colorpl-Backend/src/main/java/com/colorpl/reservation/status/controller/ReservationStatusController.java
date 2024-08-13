package com.colorpl.reservation.status.controller;

import com.colorpl.reservation.status.service.DeleteAllReservationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations/status")
@RequiredArgsConstructor
public class ReservationStatusController {

    private final DeleteAllReservationStatusService deleteAllReservationStatusService;

    @DeleteMapping
    public ResponseEntity<?> deleteAllReservationStatus() {
        deleteAllReservationStatusService.deleteAllReservationStatus();
        return ResponseEntity.noContent().build();
    }
}
