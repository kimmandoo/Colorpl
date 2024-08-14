package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.ScheduleNotFoundException;
import com.colorpl.schedule.dto.GetScheduleResponse;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import com.colorpl.schedule.repository.ReservationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetScheduleService {

    private final CustomScheduleRepository customScheduleRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;

    @Transactional(readOnly = true)
    public GetScheduleResponse getSchedule(Long id) {
        if (customScheduleRepository.existsById(id)) {
            return GetScheduleResponse.from(customScheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id)));
        }
        return GetScheduleResponse.from(reservationScheduleRepository.getSchedule(id));
    }
}
