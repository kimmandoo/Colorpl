package com.colorpl.reservation.status.controller;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.service.DeleteAllReservationStatusService;
import com.colorpl.reservation.status.service.GetReservationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/status")
@RequiredArgsConstructor
public class ReservationStatusController {

    private final DeleteAllReservationStatusService deleteAllReservationStatusService;
    private final GetReservationStatusService getReservationStatusService;

    @GetMapping("/{showScheduleId}")
    public ResponseEntity<ReservationStatus> getReservationStatusByShowScheduleId(
            @PathVariable Long showScheduleId
    ) {
        return ResponseEntity.ok(getReservationStatusService.getReservationStatusByShowScheduleId(showScheduleId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllReservationStatus() {
        deleteAllReservationStatusService.deleteAllReservationStatus();
        return ResponseEntity.noContent().build();
    }
}
