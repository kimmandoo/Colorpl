package com.colorpl.schedule.query.application;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MonthlyScheduleListRequest {

    private Integer memberId;
    private LocalDate date;
}
