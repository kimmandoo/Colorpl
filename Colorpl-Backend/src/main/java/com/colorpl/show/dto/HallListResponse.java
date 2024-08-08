package com.colorpl.show.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HallListResponse {

    private String name;
    private Integer countSeats;
    private List<ShowScheduleListItem> timetable;
}
