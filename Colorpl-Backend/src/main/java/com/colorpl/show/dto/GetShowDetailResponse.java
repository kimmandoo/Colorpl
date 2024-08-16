package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import java.time.LocalDate;
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
    private String cast;
    private String runtime;
    private Map<SeatClass, Integer> priceBySeatClass;
    private String posterImagePath;
    private Category category;
    private Map<LocalDate, Boolean> schedule;

    public static GetShowDetailResponse of(
        ShowDetail showDetail,
        Map<LocalDate, Boolean> schedule
    ) {
        return GetShowDetailResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .cast(showDetail.getCast())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .schedule(schedule)
            .build();
    }
}
