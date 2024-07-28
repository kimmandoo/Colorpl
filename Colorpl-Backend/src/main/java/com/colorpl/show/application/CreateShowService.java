package com.colorpl.show.application;

import com.colorpl.show.domain.detail.CreateShowDetailService;
import com.colorpl.show.domain.detail.ShowDetail;
import com.colorpl.show.domain.schedule.CreateShowScheduleService;
import com.colorpl.show.infra.ShowDetailApiResponse;
import com.colorpl.show.infra.ShowListApiResponse;
import com.colorpl.show.infra.ShowListApiResponse.Item;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
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

    public void create(LocalDate from, LocalDate to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        int page = 1;
        while (true) {
            ShowListApiResponse showListApiResponse = retrieveShowListApiService.retrieve(from, to,
                page++);
            if (showListApiResponse.getItems().isEmpty()) {
                break;
            }
            for (Item item : showListApiResponse.getItems()) {
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
