package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.dto.ScheduleListResponse;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import com.colorpl.schedule.dto.SearchScheduleCondition.SearchScheduleConditionBuilder;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import com.colorpl.schedule.repository.ReservationScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SearchScheduleService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CustomScheduleRepository customScheduleRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> search() {
        return search(SearchScheduleCondition.builder());
    }

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> searchMonthly(LocalDate date) {
        return search(SearchScheduleCondition.builder()
            .from(date.withDayOfMonth(1).atStartOfDay())
            .to(date.withDayOfMonth(1).plusMonths(1).atStartOfDay()));
    }

    private List<ScheduleListResponse> search(SearchScheduleConditionBuilder builder) {

        Integer memberId = memberService.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        SearchScheduleCondition condition = builder.member(member).build();

        List<CustomSchedule> customSchedules = customScheduleRepository.search(condition);

        List<ReservationSchedule> reservationSchedules = reservationScheduleRepository.search(
            condition);

        return Stream.concat(customSchedules.stream(), reservationSchedules.stream())
            .map(ScheduleListResponse::from)
            .toList();
    }
}
