package com.colorpl.show.service;

import com.colorpl.global.common.exception.ShowAlreadyExistsException;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.ShowDetailApiResponse;
import com.colorpl.show.repository.ShowDetailRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CreateShowService {

    private final ShowDetailRepository showDetailRepository;
    private final RetrieveShowDetailApiService retrieveShowDetailApiService;
    private final CreateShowDetailService createShowDetailService;
    private final CreateShowScheduleService createShowScheduleService;

    //
//    private final CreateShowDetailService createShowDetailService;
//    private final CreateShowScheduleService createShowScheduleService;
//    private final RetrieveShowDetailApiService retrieveShowDetailApiService;
//    private final RetrieveShowListApiService retrieveShowListApiService;
//    private final ShowDetailRepository showDetailRepository;
//
//    @Value("${days.to.add}")
//    private Long daysToAdd;
//
//    @Scheduled(cron = "0 0 0 * * *")
//    public void createScheduled() {
//        createByDate(LocalDate.now(), LocalDate.now().plusDays(daysToAdd));
//    }
//
//    public void createByDate(LocalDate from, LocalDate to) {
//
//        int page = 1;
//
//        ShowListApiResponse response = retrieveShowListApiService.retrieve(from, to, page++);
//
//        while (!response.getItems().isEmpty()) {
//
//            response.getItems().forEach(i -> {
//                try {
//                    create(i.getApiId());
//                } catch (ShowAlreadyExistsException ignored) {
//                    System.out.println(i.getApiId() + "duplicated");
//                }
//            });
//
//            response = retrieveShowListApiService.retrieve(from, to, page++);
//        }
//    }

    @Transactional
    public Integer createShowByApiId(String apiId) {
        return createShow(apiId);
    }

    private Integer createShow(String apiId) {
        if (showDetailRepository.existsByApiId(apiId)) {
            throw new ShowAlreadyExistsException(apiId);
        }
        ShowDetailApiResponse response = retrieveShowDetailApiService.retrieve(apiId);
        ShowDetail showDetail = createShowDetailService.createShowDetail(response.getItem());
        if (StringUtils.hasText(response.getItem().getStartDate()) &&
            StringUtils.hasText(response.getItem().getEndDate()) &&
            StringUtils.hasText(response.getItem().getSchedule())
        ) {
            String schedule = response.getItem().getSchedule();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate startDate = LocalDate.parse(response.getItem().getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(response.getItem().getEndDate(), formatter);
            createShowScheduleService.createShowSchedule(schedule, startDate, endDate, showDetail);
        }
        return showDetail.getId();
    }
}
