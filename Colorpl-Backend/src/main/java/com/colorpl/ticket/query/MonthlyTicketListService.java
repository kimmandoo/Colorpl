package com.colorpl.ticket.query;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import com.colorpl.ticket.query.MonthlyTicketListResponse.MonthlyTicketListResponseBuilder;
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
        return ticketRepository.monthlyTicketList(member, from, to).stream()
            .map(this::createResponse)
            .toList();
    }

    private MonthlyTicketListResponse createResponse(Ticket ticket) {
        MonthlyTicketListResponseBuilder builder = MonthlyTicketListResponse.builder()
            .showName(ticket.getName())
            .schedule(ticket.getDateTime())
            .seat(ticket.getSeat())
            .category(ticket.getCategory())
            .theaterName(ticket.getTheater());

        if (ticket.getReview().isPresent()) {
            return builder
                .reviewId(ticket.getReview().get().getId())
                .content(ticket.getReview().get().getContent())
                .emotion(ticket.getReview().get().getEmotion())
                .emphathy(ticket.getReview().get().getEmphathy())
                .build();
        } else {
            return builder.build();
        }
    }
}
