package com.colorpl.reservation.controller;

import com.colorpl.member.service.MemberService;
import com.colorpl.reservation.dto.ReservationRequest;
import com.colorpl.reservation.dto.ReservationResponse;
import com.colorpl.reservation.repository.ReservationDetailRepository;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.reservation.service.ReservationService;
import com.colorpl.show.repository.ShowScheduleRepositoryImpl;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;
    private final ShowScheduleRepositoryImpl showScheduleRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "멤버의 모든 예매 조회", description = "로그인 한 사용자의 모든 예매를 조회할 때 사용하는 API")
    public ResponseEntity<List<ReservationResponse>> getReservationsByMemberId() {
        Integer memberId = memberService.getCurrentMemberId();
        List<ReservationResponse> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "사용자의 특정 예매 상세 조회", description = "특정 멤버의 특정 예매를 상세 조회할 때 사용하는 API")
    public ResponseEntity<ReservationResponse> getReservationByMemberIdAndReservationId(
            @PathVariable Long reservationId) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationResponse reservation = reservationService.getReservationByMemberIdAndReservationId(
                memberId, reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PostMapping("/cancel/{reservationId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "사용자의 특정 예매 취소", description = "로그인 된 사용자가 특정 예매를 취소할 때 사용하는 API, is_refunded를 true로 변경")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(
            @PathVariable Long reservationId) {
        Integer memberId = memberService.getCurrentMemberId();
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "사용자의 예매 업데이트", description = "로그인 된 사용자가 예매를 수정하는 API, 새로운 예매 상세 추가 시 id를 빼고 입력해야 정상 작동")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long reservationId,
                                                                 @RequestBody ReservationRequest reservationRequest) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationResponse updatedReservation = reservationService.updateReservation(memberId,
                reservationId, reservationRequest);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "사용자의 예매 생성", description = "로그인 된 사용자의 예매를 생성하는 API")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest reservationRequest) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationResponse createdReservation = reservationService.createReservation(memberId,
                reservationRequest);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    // -------------관리자용 or 다른 유저 정보 조회 시 사용---------------

    @GetMapping("member/{memberId}")
    @Operation(summary = "특정 멤버의 모든 예매 조회", description = "특정 멤버의 모든 예매를 조회할 때 사용하는 API")
    public ResponseEntity<List<ReservationResponse>> getReservationsByMemberId(
            @PathVariable Integer memberId) {
        List<ReservationResponse> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 멤버의 특정 예매 상세 조회", description = "특정 멤버의 특정 예매를 상세 조회할 때 사용하는 API")
    public ResponseEntity<ReservationResponse> getReservationByMemberIdAndReservationId(
            @PathVariable Integer memberId,
            @PathVariable Long reservationId) {
        ReservationResponse reservation = reservationService.getReservationByMemberIdAndReservationId(
                memberId, reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "모든 예매 조회", description = "모든 예매를 조회할 때 사용하는 API")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/member/{memberId}")
    @Operation(summary = "모든 예매 삭제", description = "특정 사용자의 모든 예매 삭제")
    public ResponseEntity<Void> deleteAllReservationsByMemberId(@PathVariable Integer memberId) {
        reservationService.deleteAllReservationsByMemberId(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cancel/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 멤버의 특정 예매 취소", description = "특정 멤버의 특정 예매를 취소하는 API, is_refunded를 true로 변경")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(
            @PathVariable Integer memberId, @PathVariable Long reservationId) {
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 예매 업데이트", description = "특정 멤버의 특정 예매를 수정하는 API, 새로운 예매 상세 추가 시 id를 빼고 입력해야 정상 작동")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Integer memberId,
                                                                 @PathVariable Long reservationId, @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse updatedReservation = reservationService.updateReservation(memberId,
                reservationId, reservationRequest);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @PostMapping("/member/{memberId}")
    @Operation(summary = "예매 생성", description = "특정 멤버의 예매를 생성하는 API")
    public ResponseEntity<ReservationResponse> createReservation(@PathVariable Integer memberId,
                                                                 @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse createdReservation = reservationService.createReservation(memberId,
                reservationRequest);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

}
