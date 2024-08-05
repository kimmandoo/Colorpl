package com.colorpl.ticket.query;

import com.colorpl.show.domain.detail.Category;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MonthlyTicketListResponse {

    private String showName;
    private String theaterName;
    private LocalDateTime showSchedule;
    private String showSeat;
    private Category showCategory;

//    private Integer reviewId;
//    private String reviewContent;
//    private Integer reviewEmotion;
//    private Byte reviewEmphathy;
//
//    private Double theaterLatitude;
//    private Double theaterLongitude;

    @QueryProjection
    public MonthlyTicketListResponse(String showName, String theaterName,
        LocalDateTime showSchedule,
        String showSeat, Category showCategory) {
        this.showName = showName;
        this.theaterName = theaterName;
        this.showSchedule = showSchedule;
        this.showSeat = showSeat;
        this.showCategory = showCategory;
    }
}
