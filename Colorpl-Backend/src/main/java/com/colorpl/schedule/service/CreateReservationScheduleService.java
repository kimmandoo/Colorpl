package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReservationDetailNotFoundException;
import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.repository.ScheduleRepository;
import com.colorpl.schedule.dto.CreateReservationScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateReservationScheduleService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final StorageService storageService;

    public Long create(CreateReservationScheduleRequest request,
        MultipartFile attachFile) {

        Integer memberId = memberService.getCurrentMemberId();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        UploadFile uploadFile = storageService.storeFile(attachFile);

        ReservationDetail reservationDetail = reservationRepository.findDetailByIdAndMemberId(
            request.getReservationDetailId(), memberId).orElseThrow(
            ReservationDetailNotFoundException::new);

        ReservationSchedule reservationSchedule = ReservationSchedule.builder()
            .member(member)
            .image(uploadFile.getStoreFilename())
            .reservationDetail(reservationDetail)
            .build();
        scheduleRepository.save(reservationSchedule);

        return reservationSchedule.getId();
    }
}
