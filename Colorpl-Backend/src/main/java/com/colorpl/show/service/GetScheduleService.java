package com.colorpl.show.service;

import com.colorpl.reservation.status.domain.ReservationStatus;
import com.colorpl.reservation.status.service.GetReservationStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetScheduleService {

    private final GetReservationStatusService getReservationStatusService;

    @Transactional(readOnly = true)
    public Map<String, Boolean> getSchedule(Long showScheduleId) {
        ReservationStatus reservationStatus = getReservationStatusService.getReservationStatus(
            showScheduleId);
        return reservationStatus.getReserved();
    }
}
