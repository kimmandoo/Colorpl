package com.colorpl.ticket.ui;

import com.colorpl.ticket.query.MonthlyTicketListRequest;
import com.colorpl.ticket.query.MonthlyTicketListResponse;
import com.colorpl.ticket.query.MonthlyTicketListService;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tickets")
@RequiredArgsConstructor
@RestController
public class TicketController {

    private final MonthlyTicketListService monthlyTicketListService;

    @GetMapping("/")
    public List<MonthlyTicketListResponse> getMonthlyTicketList(
        @ModelAttribute MonthlyTicketListRequest request) {
        LocalDateTime from = YearMonth.of(request.getDate().getYear(), request.getDate().getMonth())
            .atDay(1).atStartOfDay();
        LocalDateTime to = YearMonth.of(request.getDate().getYear(),
            request.getDate().getMonth().plus(1)).atDay(1).atStartOfDay();
        return monthlyTicketListService.monthlyTicketList(request.getMemberId(), from, to);
    }
}
