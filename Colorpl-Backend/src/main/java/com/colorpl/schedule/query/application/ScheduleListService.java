package com.colorpl.schedule.query.application;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import com.colorpl.schedule.query.dto.ScheduleListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScheduleListService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public List<ScheduleListResponse> getScheduleList() {

        Integer memberId = memberService.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return scheduleRepository.findByMember(member).stream()
            .map(ScheduleListResponse::from)
            .toList();
    }
}
