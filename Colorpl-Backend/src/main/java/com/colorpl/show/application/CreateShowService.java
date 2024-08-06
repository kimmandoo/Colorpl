package com.colorpl.show.application;

import com.colorpl.global.common.exception.ShowAlreadyExistsException;
import com.colorpl.show.domain.detail.CreateShowDetailService;
import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.domain.detail.ShowDetailRepository;
import com.colorpl.show.domain.schedule.CreateShowScheduleService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateShowService {

    private final CreateShowDetailService createShowDetailService;
    private final CreateShowScheduleService createShowScheduleService;
    private final RetrieveShowDetailApiService retrieveShowDetailApiService;
    private final RetrieveShowListApiService retrieveShowListApiService;
    private final ShowDetailRepository showDetailRepository;

    @Value("${days.to.add}")
    private Long daysToAdd;

    @Scheduled(cron = "0 0 0 * * *")
    public void createScheduled() {
        createByDate(LocalDate.now(), LocalDate.now().plusDays(daysToAdd));
    }

    public void createByDate(LocalDate from, LocalDate to) {

        int page = 1;

        ShowListApiResponse response = retrieveShowListApiService.retrieve(from, to, page++);

        while (!response.getItems().isEmpty()) {

            response.getItems().forEach(i -> {
                try {
                    create(i.getApiId());
                } catch (ShowAlreadyExistsException ignored) {

                }
            });

            response = retrieveShowListApiService.retrieve(from, to, page++);
        }
    }

    public Long createByApiId(String apiId) {
        return create(apiId);
    }

    private Long create(String apiId) {

        if (showDetailRepository.existsByApiId(apiId)) {
            throw new ShowAlreadyExistsException();
        }

        ShowDetailApiResponse response = retrieveShowDetailApiService.retrieve(apiId);

        ShowDetail showDetail = createShowDetailService.create(response.getItem());

        if (StringUtils.hasText(response.getItem().getStartDate()) &&
            StringUtils.hasText(response.getItem().getEndDate()) &&
            StringUtils.hasText(response.getItem().getSchedule())) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate startDate = LocalDate.parse(response.getItem().getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(response.getItem().getEndDate(), formatter);
            String schedule = response.getItem().getSchedule();

            createShowScheduleService.create(showDetail, startDate, endDate, schedule);
        }

        return showDetail.getId();
    }
}
