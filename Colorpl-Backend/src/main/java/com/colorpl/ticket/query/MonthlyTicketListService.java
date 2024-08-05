package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.ticket.domain.TicketRepository;
import java.time.LocalDateTime;
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

    @Transactional(readOnly = true)
    public List<MonthlyTicketListResponse> monthlyTicketList(Integer memberId, LocalDateTime from,
        LocalDateTime to) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        return ticketRepository.monthlyTicketList(member, from, to);
    }
}
