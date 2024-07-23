package com.colorpl.reservation.service;

import com.colorpl.member.Member;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.dto.ReservationDetailDTO;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.show.domain.schedule.ShowSchedule;
import com.colorpl.show.repository.ShowScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ShowScheduleRepository showScheduleRepository;

    @Transactional(readOnly = true)
    public List<ReservationDTO> getReservationsByMemberId(Integer memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(this::toReservationDTO)
                .collect(Collectors.toList());
    }

    //모든 예매 삭제
    @Transactional
    public void deleteAllReservationsByMemberId(Integer memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        reservationRepository.deleteAll(reservations);
    }

    @Transactional
    public void deleteReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("예매 내역이 없습니다."));

        reservationRepository.delete(reservation);
    }

    //특정 멤버의 특정 예약을 삭제
    @Transactional
    public void deleteReservationByMemberIdAndReservationId(Integer memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .filter(res -> res.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException("예약이 없거나 멤버가 일치하지 않습니다."));

        reservationRepository.delete(reservation);
    }

    @Transactional
    public void updateReservationDetail(Integer memberId, Long reservationDetailId, ReservationDetailDTO reservationDetailDTO) {
        ReservationDetail reservationDetail = reservationRepository.findDetailByIdAndMemberId(reservationDetailId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("예매 상세가 없습니다."));
        
        ShowSchedule showSchedule = showScheduleRepository.findById(reservationDetailDTO.getShowScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));

        reservationDetail.updateDetail(reservationDetailDTO.getRow(),reservationDetailDTO.getCol(),showSchedule);
    }

    @Transactional
    public void addReservation(Integer memberId, ReservationDTO reservationDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        Reservation reservation = Reservation.builder()
                .member(member)
                .date(reservationDTO.getDate())
                .amount(reservationDTO.getAmount())
                .comment(reservationDTO.getComment())
                .isRefunded(reservationDTO.isRefunded())
                .build();

        List<ReservationDetail> reservationDetails = reservationDTO.getReservationDetails()
                .stream()
                .map(detailDTO -> {
                    ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
                            .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));

                    return ReservationDetail.builder()
                            .row(detailDTO.getRow())
                            .col(detailDTO.getCol())
                            .showSchedule(showSchedule)
                            .reservation(reservation)
                            .build();
                })
                .toList();

        reservation.getReservationDetails().addAll(reservationDetails);
        reservationRepository.save(reservation);
    }

    //--DTO로 변환--
    private ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .memberId(reservation.getMember().getId())
                .date(reservation.getDate())
                .amount(reservation.getAmount())
                .comment(reservation.getComment())
                .isRefunded(reservation.isRefunded())
                .reservationDetails(reservation.getReservationDetails()
                        .stream()
                        .map(this::toReservationDetailDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private ReservationDetailDTO toReservationDetailDTO(ReservationDetail reservationDetail) {
        return ReservationDetailDTO.builder()
                .id(reservationDetail.getId())
                .row(reservationDetail.getRow())
                .col(reservationDetail.getCol())
                .showScheduleId(reservationDetail.getShowSchedule().getId())
                .build();
    }
}
