package com.colorpl.reservation.controller;


import com.colorpl.member.service.MemberService;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "멤버의 모든 예매 조회", description = "로그인 한 사용자의 모든 예매를 조회 할 때 사용하는 API")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMemberId() {
        Integer memberId = memberService.getCurrentMemberId();
        List<ReservationDTO> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "사용자의 특정 예매 상세 조회", description = "특정 멤버의 특정 예매를 상세 조회 할 때 사용하는 API")
    public ResponseEntity<ReservationDTO> getReservationByMemberIdAndReservationId(@PathVariable Long reservationId) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationDTO reservation = reservationService.getReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PostMapping("/cancel/{reservationId}")
    @Operation(summary = "사용자의 특정 예매 취소", description = "로그인 된 사용자가 특정 예매를 취소할때 사용하는 API, is_refunded를 true로 변경")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(@PathVariable Long reservationId) {
        Integer memberId = memberService.getCurrentMemberId();
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/{reservationId}")
    @Operation(summary = "사용자의 예매 업데이트", description = "로그인 된 사용자가 예매를 수정하는 API, 새로운 예매 상세 추가시 id를 빼고 in해야 정상 작동")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationDTO updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "사용자의 예매 생성", description = "로그인 된 사용자의 예매를 생성하는 API")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Integer memberId = memberService.getCurrentMemberId();
        ReservationDTO createdReservation = reservationService.createReservation(memberId, reservationDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }




    //-------------관리자용 or 다른 유저 정보 조회시 사용---------------
    // 특정 멤버의 모든 예매 조회
    @GetMapping("member/{memberId}")
    @Operation(summary = "특정 멤버의 모든 예매 조회", description = "특정 멤버의 모든 예매 조회 할 때 사용하는 API")
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMemberId(@PathVariable Integer memberId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // 특정 사용자의 특정 예매 조회
    @GetMapping("/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 멤버의 특정 예매 상세 조회", description = "특정 멤버의 특정 예매를 상세 조회 할 때 사용하는 API")
    public ResponseEntity<ReservationDTO> getReservationByMemberIdAndReservationId(
        @PathVariable Integer memberId,
        @PathVariable Long reservationId) {
        ReservationDTO reservation = reservationService.getReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // 모든 예매 조회
    @GetMapping("/all")
    @Operation(summary = "모든 예매 조회", description = "모든 예매를 조회할 때 사용하는 API")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // 모든 예매 삭제(테스트 완료)
    @DeleteMapping("/member/{memberId}")
    @Operation(summary = "모든 예매 삭제", description = "특정 사용자의 모든 예매 삭제")
    public ResponseEntity<Void> deleteAllReservationsByMemberId(@PathVariable Integer memberId) {
        reservationService.deleteAllReservationsByMemberId(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 특정 멤버의 특정 예매 취소(테스트 완료)
    @PostMapping("/cancel/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 멤버의 특정 예매 취소", description = "특정 멤버의 특정 예매를 취소하는 API, is_refunded를 true로 변경")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(@PathVariable Integer memberId, @PathVariable Long reservationId) {
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // 특정 예매 업데이트(테스트 완료)
    @PutMapping("/member/{memberId}/reservation/{reservationId}")
    @Operation(summary = "특정 예매 업데이트", description = "특정 멤버의 특정 예매를 수정하는 API, 새로운 예매 상세 추가시 id를 빼고 in해야 정상 작동")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Integer memberId, @PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }
    // 예매 생성(테스트 완료)
    @PostMapping("/member/{memberId}")
    @Operation(summary = "예매 생성", description = "특정 멤버의 예매를 생성하는 API")
    public ResponseEntity<ReservationDTO> createReservation(@PathVariable Integer memberId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(memberId, reservationDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }


}
