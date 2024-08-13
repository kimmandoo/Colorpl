package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.ScheduleNotFoundException;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.dto.UpdateCustomScheduleRequest;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateCustomScheduleService {

    private final MemberService memberService;
    private final CustomScheduleRepository customScheduleRepository;

    @Transactional
    public void updateCustomSchedule(Long scheduleId, UpdateCustomScheduleRequest request,
        MultipartFile file) {
        Integer memberId = memberService.getCurrentMemberId();
        CustomSchedule customSchedule = customScheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
        if (!customSchedule.getMember().getId().equals(memberId)) {
            throw new MemberMismatchException();
        }

        // 이미지 변경 처리
        if (file != null) {
            // 이미지 삭제
            // 이미지 저장 후 파일 이름 변경
        }

        customSchedule.updateCustomSchedule(
            request.getSeat(),
            request.getDateTime(),
            request.getName(),
            request.getCategory(),
            request.getLocation(),
            request.getLatitude(),
            request.getLongitude()
        );
    }
}
