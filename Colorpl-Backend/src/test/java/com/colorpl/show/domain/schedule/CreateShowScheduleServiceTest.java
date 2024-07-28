package com.colorpl.show.domain.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.show.application.RetrieveShowDetailApiService;
import com.colorpl.show.domain.detail.CreateShowDetailService;
import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.application.ShowDetailApiResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CreateShowScheduleServiceTest {

    @Autowired
    private CreateShowDetailService createShowDetailService;
    @Autowired
    private CreateShowScheduleService createShowScheduleService;
    @Autowired
    private RetrieveShowDetailApiService retrieveShowDetailApiService;
    @Autowired
    private ShowScheduleRepository showScheduleRepository;

    @Test
    void create() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        ShowDetailApiResponse showDetailApiResponse = retrieveShowDetailApiService.retrieve(
            "PF233138");
        ShowDetail showDetail = createShowDetailService.create(showDetailApiResponse.getItem());
        createShowScheduleService.create(showDetail,
            LocalDate.parse(showDetailApiResponse.getItem().getStartDate(), formatter),
            LocalDate.parse(showDetailApiResponse.getItem().getEndDate(), formatter),
            showDetailApiResponse.getItem().getSchedule());
        List<ShowSchedule> showSchedules = showScheduleRepository.findByShowDetail(showDetail);
        assertThat(showSchedules.get(0).getDateTime()).isEqualTo(
            LocalDateTime.of(2024, 1, 28, 14, 0));
        assertThat(showSchedules.get(1).getDateTime()).isEqualTo(
            LocalDateTime.of(2024, 1, 28, 18, 0));
        assertThat(showSchedules.get(2).getDateTime()).isEqualTo(
            LocalDateTime.of(2024, 1, 30, 19, 30));
    }
}