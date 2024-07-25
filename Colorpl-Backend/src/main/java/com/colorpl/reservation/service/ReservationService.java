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
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
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
                .map(ReservationDTO::toReservationDTO)
                .collect(Collectors.toList());
    }

    // 모든 예매 삭제
    @Transactional
    public void deleteAllReservationsByMemberId(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        // 각 예약을 member에서 제거
        reservations.forEach(member::removeReservation);

        // 모든 예약 삭제
        reservationRepository.deleteAll(reservations);
    }
    //예매 취소
    @Transactional
    public void cancelReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("예매 내역이 없습니다."));

        reservation.updateRefundState(true);  // is_refunded 필드를 true로 설정
        reservationRepository.save(reservation);  // 변경 사항을 저장
    }

    //특정 멤버의 특정 예약을 취소
    @Transactional
    public void cancelReservationByMemberIdAndReservationId(Integer memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .filter(res -> res.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException("예약이 없거나 멤버가 일치하지 않습니다."));

        reservation.updateRefundState(true);  // is_refunded 필드를 true로 설정
        reservationRepository.save(reservation);
    }

//    @Transactional
//    public ReservationDTO updateReservation(Integer memberId, Long reservationId, ReservationDTO reservationDTO) {
//        // 예약을 가져오고 멤버 ID를 확인
//        Reservation reservation = reservationRepository.findById(reservationId)
//            .filter(res -> res.getMember().getId().equals(memberId))
//            .orElseThrow(() -> new IllegalArgumentException("예약이 없거나 멤버가 일치하지 않습니다."));
//
//        // 기존 예약 정보를 업데이트
//        reservation.updateReservation(reservationDTO.getDate(), reservationDTO.getAmount(), reservationDTO.getComment(), reservationDTO.isRefunded());
//
//        // 새로 추가된 예약 상세 정보를 저장
//        Set<Long> newReservationDetailIds = reservationDTO.getReservationDetails().stream()
//            .map(ReservationDetailDTO::getId) // ID를 추출
//            .collect(Collectors.toSet());
//
//        // 기존 예약 상세 항목을 업데이트하거나 삭제
//        reservation.getReservationDetails().forEach(existingDetail -> {
//            if (newReservationDetailIds.contains(existingDetail.getId())) {
//                // 새로 받은 DTO로 업데이트
//                ReservationDetailDTO detailDTO = reservationDTO.getReservationDetails().stream()
//                    .filter(dto -> dto.getId().equals(existingDetail.getId()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("예매 상세가 없습니다."));
//
//                ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
//                    .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));
//
//                existingDetail.updateDetail(detailDTO.getRow(), detailDTO.getCol(), showSchedule);
//            } else {
//                // 기존 예약 상세가 새 데이터에 없으면 삭제
//                reservation.removeReservationDetail(existingDetail);
//            }
//        });
//
//        // 새로운 예약 상세 항목 추가
//        List<ReservationDetail> newReservationDetails = reservationDTO.getReservationDetails().stream()
//            .filter(detailDTO -> detailDTO.getId() == null) // 새로 추가된 항목 (ID가 없으면 새 항목)
//            .map(detailDTO -> {
//                ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
//                    .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));
//
//                return ReservationDetail.builder()
//                    .row(detailDTO.getRow())
//                    .col(detailDTO.getCol())
//                    .showSchedule(showSchedule)
//                    .reservation(reservation)
//                    .build();
//            })
//            .toList();
//
//        newReservationDetails.forEach(reservation::addReservationDetail);
//
//        Reservation updatedReservation = reservationRepository.save(reservation);
//
//        // 변경된 예약 저장 및 반환
//
//        return ReservationDTO.toReservationDTO(updatedReservation);
//    }
    @Transactional
    public ReservationDTO updateReservation(Integer memberId, Long reservationId, ReservationDTO reservationDTO) {
        // 예약을 가져오고 멤버 ID를 확인
        Reservation reservation = reservationRepository.findById(reservationId)
            .filter(res -> res.getMember().getId().equals(memberId))
            .orElseThrow(() -> new IllegalArgumentException("예약이 없거나 멤버가 일치하지 않습니다."));

        // 기존 예약 정보를 업데이트
        reservation.updateReservation(reservationDTO.getDate(), reservationDTO.getAmount(), reservationDTO.getComment(), reservationDTO.isRefunded());

        // 새로 추가된 예약 상세 정보를 저장
        Set<Long> newReservationDetailIds = reservationDTO.getReservationDetails().stream()
            .map(ReservationDetailDTO::getId) // ID를 추출
            .filter(Objects::nonNull) // ID가 null이 아닌 것만 필터링
            .collect(Collectors.toSet());

        // 기존 예약 상세 항목을 안전하게 수정하기 위해 컬렉션을 복사
        List<ReservationDetail> existingDetails = new ArrayList<>(reservation.getReservationDetails());

        // 기존 예약 상세 항목을 업데이트하거나 삭제
        existingDetails.forEach(existingDetail -> {
            if (newReservationDetailIds.contains(existingDetail.getId())) {
                // 새로 받은 DTO로 업데이트
                ReservationDetailDTO detailDTO = reservationDTO.getReservationDetails().stream()
                    .filter(dto -> dto.getId().equals(existingDetail.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("예매 상세가 없습니다."));

                ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));

                existingDetail.updateDetail(detailDTO.getRow(), detailDTO.getCol(), showSchedule);
            } else {
                // 기존 예약 상세가 새 데이터에 없으면 삭제
                reservation.removeReservationDetail(existingDetail);
            }
        });

        // 새로운 예약 상세 항목 추가
        List<ReservationDetail> newReservationDetails = reservationDTO.getReservationDetails().stream()
            .filter(detailDTO -> detailDTO.getId() == null) // ID가 없는 항목은 새 항목
            .map(detailDTO -> {
                ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("공연 일정이 없습니다."));

                return ReservationDetail.builder()
                    .row(detailDTO.getRow())
                    .col(detailDTO.getCol())
                    .showSchedule(showSchedule)
                    .reservation(reservation) // 여기서 `reservation` 설정
                    .build();
            })
            .toList();

        newReservationDetails.forEach(reservation::addReservationDetail);

        // 예약 저장 및 DTO 변환
        Reservation updatedReservation = reservationRepository.save(reservation);

        // 변경된 예약 반환
        return ReservationDTO.toReservationDTO(updatedReservation);
    }


    @Transactional
    public ReservationDTO createReservation(Integer memberId, ReservationDTO reservationDTO) {
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

        reservationDetails.forEach(reservation::addReservationDetail);

        // Member와 Reservation의 관계 설정
        member.addReservation(reservation);
        Reservation createdReservation = reservationRepository.save(reservation);

        // Reservation 저장
        return ReservationDTO.toReservationDTO(createdReservation);
    }


}
