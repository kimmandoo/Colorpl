package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.ScheduleNotFoundException;
import com.colorpl.global.storage.StorageService;
import com.colorpl.global.storage.UploadFile;
import com.colorpl.member.service.MemberService;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.dto.UpdateCustomScheduleRequest;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdateCustomScheduleService {

    private final MemberService memberService;
    private final CustomScheduleRepository customScheduleRepository;
    private final StorageService storageService;

    @Transactional
    public void updateCustomSchedule(Long scheduleId, UpdateCustomScheduleRequest request,
        MultipartFile file) {
        Integer memberId = memberService.getCurrentMemberId();
        CustomSchedule customSchedule = customScheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
        if (!customSchedule.getMember().getId().equals(memberId)) {
            throw new MemberMismatchException();
        }

        if (file != null) {
            storageService.deleteFile(customSchedule.getImage());
            UploadFile uploadFile = storageService.storeFile(file);
            customSchedule.updateImage(uploadFile.getStoreFilename());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

        customSchedule.updateCustomSchedule(
            request.getSeat(),
            LocalDateTime.parse(request.getDateTime(), formatter),
            request.getName(),
            request.getCategory(),
            request.getLocation(),
            request.getLatitude(),
            request.getLongitude()
        );
    }
}
