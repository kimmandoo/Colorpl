package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.SeatClass;
import com.colorpl.show.domain.ShowDetail;
import java.util.List;
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
    private List<SeatResponse> seats;

    @Getter
    @Builder
    public static class SeatResponse {

        private Integer row;
        private Integer col;
        private SeatClass seatClass;
    }

    public static GetShowDetailResponse from(ShowDetail showDetail) {
        return GetShowDetailResponse.builder()
            .id(showDetail.getId())
            .name(showDetail.getName())
            .runtime(showDetail.getRuntime())
            .priceBySeatClass(showDetail.getPriceBySeatClass())
            .posterImagePath(showDetail.getPosterImagePath())
            .category(showDetail.getCategory())
            .seats(showDetail.getSeats().stream().map(s -> SeatResponse.builder()
                .row(s.getRow())
                .col(s.getCol())
                .seatClass(s.getSeatClass())
                .build()).toList())
            .build();
    }
}
