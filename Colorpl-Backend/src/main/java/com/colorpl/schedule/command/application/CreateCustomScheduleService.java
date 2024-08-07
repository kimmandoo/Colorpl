package com.colorpl.schedule.command.application;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import com.colorpl.schedule.ui.CreateCustomScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateCustomScheduleService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ScheduleRepository scheduleRepository;

    public Long createCustomSchedule(CreateCustomScheduleRequest request) {

        Integer memberId = memberService.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        CustomSchedule customSchedule = CustomSchedule.builder()
            .member(member)
            .seat(request.getSeat())
            .dateTime(request.getDateTime())
            .name(request.getName())
            .category(request.getCategory())
            .location(request.getLocation())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .build();
        scheduleRepository.save(customSchedule);

        return customSchedule.getId();
    }
}
