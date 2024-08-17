package com.colorpl.reservation.service;

import com.colorpl.global.common.exception.*;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.dto.ReservationRequest;
import com.colorpl.reservation.dto.ReservationResponse;
import com.colorpl.reservation.dto.ReservationDetailRequest;
import com.colorpl.reservation.dto.ReservationDetailResponse;
import com.colorpl.reservation.repository.ReservationDetailRepository;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.reservation.status.service.DeleteReservationStatusService;
import com.colorpl.reservation.status.service.DisableReservationService;
import com.colorpl.reservation.status.service.EnableReservationService;
import com.colorpl.schedule.domain.ReservationSchedule;
import com.colorpl.schedule.repository.ReservationScheduleRepository;
import com.colorpl.schedule.service.DeleteReservationScheduleService;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.repository.ShowScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ShowScheduleRepository showScheduleRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final EnableReservationService enableReservationService;
    private final DisableReservationService disableReservationService;
    private final DeleteReservationStatusService deleteReservationStatusService;
    private final DeleteReservationScheduleService deleteReservationScheduleService;
    private final ReservationScheduleRepository reservationScheduleRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByMemberId(Integer memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException();
        }

        return reservations.stream()
                .map(ReservationResponse::toReservationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservationByMemberIdAndReservationId(Integer memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(res -> res.getMember().getId().equals(memberId))
                .orElseThrow(ReservationNotFoundException::new);

        return ReservationResponse.toReservationResponse(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException();
        }

        return reservations.stream()
                .map(ReservationResponse::toReservationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllReservationsByMemberId(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        reservations.forEach(
                reservation -> reservation.getReservationDetails()
                        .forEach(reservationDetail -> enableReservationService.enableReservation(
                                reservationDetail.getShowSchedule().getId(),
                                Integer.valueOf(reservationDetail.getRow()),
                                Integer.valueOf(reservationDetail.getCol())
                        )));

        reservations.forEach(member::removeReservation);

        reservationRepository.deleteAll(reservations);
    }

    @Transactional
    public void cancelReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        reservation.updateRefundState(true);  // is_refunded 필드를 true로 설정
        reservationRepository.save(reservation);

        Optional<ReservationSchedule> reservationSchedule = reservationScheduleRepository.findByReservation(reservation);
        deleteReservationScheduleService.deleteReservationSchedule(reservationSchedule.orElseThrow().getId());

        reservation.getReservationDetails()
                .forEach(reservationDetail -> enableReservationService.enableReservation(
                        reservationDetail.getShowSchedule().getId(),
                        Integer.valueOf(reservationDetail.getRow()),
                        Integer.valueOf(reservationDetail.getCol())
                ));
        reservationDetailRepository.deleteAll(reservation.getReservationDetails());
        reservationRepository.delete(reservation);

    }

    @Transactional
    public void cancelReservationByMemberIdAndReservationId(Integer memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(res -> res.getMember().getId().equals(memberId))
                .orElseThrow(MemberMismatchException::new);

        reservation.updateRefundState(true);  // is_refunded 필드를 true로 설정
        reservationRepository.save(reservation);

        reservation.getReservationDetails()
                .forEach(reservationDetail -> enableReservationService.enableReservation(
                        reservationDetail.getShowSchedule().getId(),
                        Integer.valueOf(reservationDetail.getRow()),
                        Integer.valueOf(reservationDetail.getCol())
                ));
    }

    @Transactional
    public ReservationResponse updateReservation(Integer memberId, Long reservationId, ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(res -> res.getMember().getId().equals(memberId))
                .orElseThrow(MemberMismatchException::new);

        reservation.updateReservation(reservationRequest.getDate(), reservationRequest.getAmount(),
                reservationRequest.getComment(), reservationRequest.isRefunded());

        Set<Long> newReservationDetailIds = reservationRequest.getReservationDetails().stream()
                .map(ReservationDetailRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<ReservationDetail> existingDetails = new ArrayList<>(reservation.getReservationDetails());

        existingDetails.stream()
                .filter(existingDetail -> !newReservationDetailIds.contains(existingDetail.getId()))
                .forEach(existingDetail -> {
                    reservation.removeReservationDetail(existingDetail);
                    reservationDetailRepository.delete(existingDetail);

                    enableReservationService.enableReservation(
                            existingDetail.getShowSchedule().getId(),
                            Integer.valueOf(existingDetail.getRow()),
                            Integer.valueOf(existingDetail.getCol())
                    );
                });

        List<ReservationDetail> newReservationDetails = reservationRequest.getReservationDetails()
                .stream()
                .filter(detailDTO -> detailDTO.getId() == null)
                .map(detailDTO -> {
                    ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
                            .orElseThrow(ShowScheduleNotFoundException::new);

                    ReservationDetail newDetail = ReservationDetail.builder()
                            .row(detailDTO.getRow())
                            .col(detailDTO.getCol())
                            .showSchedule(showSchedule)
                            .reservation(reservation)
                            .build();

                    reservation.addReservationDetail(newDetail);
                    return newDetail;
                })
                .toList();

        reservationDetailRepository.saveAll(newReservationDetails);

        newReservationDetails.forEach(
                reservationDetail -> disableReservationService.disableReservation(
                        reservationDetail.getShowSchedule().getId(),
                        Integer.valueOf(reservationDetail.getRow()),
                        Integer.valueOf(reservationDetail.getCol())
                ));

        Reservation updatedReservation = reservationRepository.save(reservation);

        return ReservationResponse.toReservationResponse(updatedReservation);
    }

    @Transactional
    public ReservationResponse createReservation(Integer memberId, ReservationRequest reservationRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Reservation reservation = Reservation.builder()
                .member(member)
                .date(reservationRequest.getDate())
                .amount(reservationRequest.getAmount())
                .comment(reservationRequest.getComment())
                .isRefunded(reservationRequest.isRefunded())
                .build();

        List<ReservationDetail> reservationDetails = reservationRequest.getReservationDetails()
                .stream()
                .map(detailDTO -> {
                    ShowSchedule showSchedule = showScheduleRepository.findById(detailDTO.getShowScheduleId())
                            .orElseThrow(ShowScheduleNotFoundException::new);

                    ReservationDetail reservationDetail = ReservationDetail.builder()
                            .row(detailDTO.getRow())
                            .col(detailDTO.getCol())
                            .showSchedule(showSchedule)
                            .reservation(reservation)
                            .build();

                    reservation.addReservationDetail(reservationDetail);

                    return reservationDetail;
                })
                .toList();

        reservationDetails.forEach(reservationDetail -> {
            ReservationDetail lockedDetail = reservationDetailRepository.findByShowScheduleIdAndRowAndColForUpdate(
                    reservationDetail.getShowSchedule().getId(),
                    reservationDetail.getRow(),
                    reservationDetail.getCol());

            if (lockedDetail != null) {
                throw new AlreadyReservedSeatException() ;
            }
        });

        Reservation createdReservation = reservationRepository.save(reservation);
        reservationDetailRepository.saveAll(reservationDetails);

        reservation.getReservationDetails()
                .forEach(reservationDetail -> disableReservationService.disableReservation(
                        reservationDetail.getShowSchedule().getId(),
                        Integer.valueOf(reservationDetail.getRow()),
                        Integer.valueOf(reservationDetail.getCol())
                ));

        return ReservationResponse.toReservationResponse(createdReservation);
    }

    @Transactional(readOnly = true)
    public long countReservedSeatsByShowScheduleId(Long showScheduleId) {
        List<Reservation> reservations = reservationRepository.findByReservationDetailsShowScheduleId(showScheduleId);

        return reservations.stream()
                .flatMap(reservation -> reservation.getReservationDetails().stream())
                .filter(detail -> detail.getShowSchedule().getId().equals(showScheduleId))
                .count();
    }
}
