package com.colorpl.schedule.query.application;

import com.colorpl.schedule.command.domain.ScheduleRepository;
import com.colorpl.schedule.query.dto.ScheduleListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ScheduleListService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleListResponse> scheduleList() {
        return scheduleRepository.findAll().stream()
            .map(ScheduleListResponse::from)
            .toList();
    }
}
