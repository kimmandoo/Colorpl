package com.colorpl.show.service;

import com.colorpl.reservation.status.domain.ReservationStatus.Item;
import com.colorpl.reservation.status.service.GetReservationStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetShowScheduleService {

    private final GetReservationStatusService getReservationStatusService;

    @Transactional(readOnly = true)
    public Map<String, Item> getShowSchedule(Long showScheduleId) {
        return getReservationStatusService.getReservationStatus(showScheduleId).getReserved();
    }
}
