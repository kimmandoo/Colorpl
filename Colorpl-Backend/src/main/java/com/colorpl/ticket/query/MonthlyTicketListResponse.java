package com.colorpl.ticket.query;

import com.colorpl.show.domain.detail.Category;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class MonthlyTicketListResponse {

    private String showName;
    private LocalDateTime schedule;
    private String seat;
    private Category category;
    private String theaterName;
    private Double latitude;
    private Double longitude;
    private Long reviewId;
    private String content;
    private Byte emotion;
    private Integer emphathy;
}
