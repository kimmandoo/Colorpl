package com.colorpl.schedule.command.application;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateCustomScheduleService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    public Long createCustomSchedule(CreateCustomScheduleRequest request) {

        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
            MemberNotFoundException::new);

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
