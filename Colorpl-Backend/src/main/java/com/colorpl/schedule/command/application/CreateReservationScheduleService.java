package com.colorpl.schedule.command.application;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReservationDetailNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateReservationScheduleService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;

    public Long createReservationSchedule(CreateReservationScheduleRequest request) {

        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
            MemberNotFoundException::new);

        ReservationDetail reservationDetail = reservationRepository.findDetailByIdAndMemberId(
            request.getReservationDetailId(), request.getMemberId()).orElseThrow(
            ReservationDetailNotFoundException::new);

        ReservationSchedule reservationSchedule = ReservationSchedule.builder()
            .member(member)
            .reservationDetail(reservationDetail)
            .build();
        scheduleRepository.save(reservationSchedule);

        return reservationSchedule.getId();
    }
}
