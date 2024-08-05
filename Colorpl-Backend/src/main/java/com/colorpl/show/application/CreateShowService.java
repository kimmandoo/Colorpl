package com.colorpl.show.application;

import com.colorpl.show.application.ShowListApiResponse.Item;
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

    @Scheduled(cron = "0 27 17 * * *")
    public void create() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        int page = 1;
        while (true) {
            ShowListApiResponse showListApiResponse = retrieveShowListApiService.retrieve(
                LocalDate.now(), LocalDate.now().plusDays(daysToAdd), page++);
            if (showListApiResponse.getItems().isEmpty()) {
                break;
            }
            for (Item item : showListApiResponse.getItems()) {
                if (showDetailRepository.findByApiId(item.getApiId()).isEmpty()) {
                    ShowDetailApiResponse showDetailApiResponse = retrieveShowDetailApiService.retrieve(
                        item.getApiId());
                    ShowDetail showDetail = createShowDetailService.create(
                        showDetailApiResponse.getItem());
                    createShowScheduleService.create(showDetail,
                        LocalDate.parse(showDetailApiResponse.getItem().getStartDate(), formatter),
                        LocalDate.parse(showDetailApiResponse.getItem().getEndDate(), formatter),
                        showDetailApiResponse.getItem().getSchedule());
                }
            }
        }
    }

    public Long createByShowApiId(String showApiId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        if (showDetailRepository.findByApiId(showApiId).isEmpty()) {
            ShowDetailApiResponse showDetailApiResponse = retrieveShowDetailApiService.retrieve(
                showApiId);
            ShowDetail showDetail = createShowDetailService.create(
                showDetailApiResponse.getItem());
            createShowScheduleService.create(showDetail,
                LocalDate.parse(showDetailApiResponse.getItem().getStartDate(), formatter),
                LocalDate.parse(showDetailApiResponse.getItem().getEndDate(), formatter),
                showDetailApiResponse.getItem().getSchedule());
            return showDetail.getId();
        }
        return null;
    }
}
