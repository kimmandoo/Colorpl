package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.ticket.domain.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MonthlyTicketListService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;

    public List<MonthlyTicketListResponse> monthlyTicketList(MonthlyTicketListRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow();
        return ticketRepository.monthlyTicketList(member, request.getPeriod().getFrom(),
            request.getPeriod().getTo());
    }
}
