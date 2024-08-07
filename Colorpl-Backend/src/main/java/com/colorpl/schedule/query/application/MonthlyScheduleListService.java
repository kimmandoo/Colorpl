package com.colorpl.schedule.query.application;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.query.dao.CustomScheduleRepository;
import com.colorpl.schedule.query.dao.ReservationScheduleRepository;
import com.colorpl.schedule.query.dto.ScheduleListResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class MonthlyScheduleListService {

    private final CustomScheduleRepository customScheduleRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ReservationScheduleRepository reservationScheduleRepository;

    public List<ScheduleListResponse> monthlyScheduleList(LocalDateTime from, LocalDateTime to) {

        Integer memberId = memberService.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        List<CustomSchedule> customSchedules = customScheduleRepository.findByMemberAndDateTimeBetween(
            member, from, to);

        List<ReservationSchedule> reservationSchedules = reservationScheduleRepository.monthlyReservationScheduleList(
            member, from, to);

        return Stream.concat(customSchedules.stream(), reservationSchedules.stream())
            .map(ScheduleListResponse::from)
            .toList();
    }
}
