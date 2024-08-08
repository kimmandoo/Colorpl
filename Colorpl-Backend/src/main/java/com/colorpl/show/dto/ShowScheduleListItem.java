package com.colorpl.show.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowScheduleListItem {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer remainingSeats;
}
