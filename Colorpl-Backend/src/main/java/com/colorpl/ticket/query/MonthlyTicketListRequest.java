package com.colorpl.ticket.query;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MonthlyTicketListRequest {

    private Integer memberId;
    private LocalDate date;
}
