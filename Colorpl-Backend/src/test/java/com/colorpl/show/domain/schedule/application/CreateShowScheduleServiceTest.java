package com.colorpl.show.domain.schedule.application;

import com.colorpl.show.application.CreateShowService;
import com.colorpl.show.domain.Show;
import com.colorpl.show.domain.ShowRepository;
import com.colorpl.show.domain.schedule.domain.ShowSchedule;
import com.colorpl.show.domain.schedule.domain.ShowScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CreateShowScheduleServiceTest {

    @Autowired
    CreateShowService createShowService;
    @Autowired
    CreateShowScheduleService createShowScheduleService;
    @Autowired
    ShowRepository showRepository;
    @Autowired
    ShowScheduleRepository showScheduleRepository;

    @Test
    void createShowSchedules() {
        Integer showId = createShowService.createShow("PF132236");
        Show show = showRepository.findById(showId).orElseThrow();
        List<Integer> showScheduleIds = createShowScheduleService.createShowSchedules(show, show.getSchedule());
        for (Integer showScheduleId : showScheduleIds) {
            ShowSchedule showSchedule = showScheduleRepository.findById(showScheduleId).orElseThrow();
            System.out.println(showSchedule.getDate());
        }
    }

}
