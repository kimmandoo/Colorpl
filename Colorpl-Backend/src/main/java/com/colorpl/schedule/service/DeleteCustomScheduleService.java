package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.ScheduleNotFoundException;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCustomScheduleService {

    private final CustomScheduleRepository customScheduleRepository;

    @Transactional
    public void deleteCustomSchedule(Long id) {

        CustomSchedule customSchedule = customScheduleRepository.findById(id)
            .orElseThrow(() -> new ScheduleNotFoundException(id));

        // 이미지 삭제

        customScheduleRepository.deleteById(id);
    }
}
