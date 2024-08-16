package com.colorpl.schedule.service;

import com.colorpl.schedule.repository.ReservationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteReservationScheduleService {

    private final ReservationScheduleRepository reservationScheduleRepository;

    @Transactional
    public void deleteReservationSchedule(Long id) {
        reservationScheduleRepository.deleteById(id);
    }
}
