package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetShowDetailResponse {

    private Integer id;
    private String name;
    private String runtime;
    private Map<SeatClass, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;
    private Map<LocalDate, Boolean> schedule;

    @Getter
    @Builder
    public static class SeatResponse {

        private Integer row;
        private Integer col;
        private SeatClass seatClass;
    }

    public static GetShowDetailResponse from(ShowDetail showDetail) {

        Map<LocalDate, Boolean> schedule = new HashMap<>();

        LocalDate date = LocalDate.now();
        for (int i = 0; i < 14; i++) {

            if (showDetail.getShowSchedules().stream()
                .map(showSchedule -> showSchedule.getDateTime().toLocalDate()).toList()
                .contains(date)) {
                schedule.put(date, true);
            } else {
                schedule.put(date, false);
            }
            date = date.plusDays(1);
        }

        return GetShowDetailResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .schedule(schedule)
            .build();
    }
}
