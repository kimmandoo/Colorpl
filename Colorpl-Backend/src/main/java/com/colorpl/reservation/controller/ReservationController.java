package com.colorpl.reservation.controller;


import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.service.ReservationService;
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

    // 특정 멤버의 모든 예매 조회(테스트 완료)
    @GetMapping("/member/{memberId}")
//    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMemberId(@PathVariable Integer memberId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // 특정 사용자의 특정 예매 조회
    @GetMapping("/member/{memberId}/reservation/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservationByMemberIdAndReservationId(
        @PathVariable Integer memberId,
        @PathVariable Long reservationId) {
        ReservationDTO reservation = reservationService.getReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // 모든 예매 조회
    @GetMapping("/all")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // 모든 예매 삭제(테스트 완료)
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteAllReservationsByMemberId(@PathVariable Integer memberId) {
        reservationService.deleteAllReservationsByMemberId(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 특정 예매 취소(테스트 완료)
    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<Void> cancelReservationById(@PathVariable Long reservationId) {
        reservationService.cancelReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 멤버의 특정 예매 취소(테스트 완료)
    @PostMapping("/cancel/member/{memberId}/reservation/{reservationId}")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(@PathVariable Integer memberId, @PathVariable Long reservationId) {
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 예매 업데이트(테스트 완료)
    @PutMapping("/member/{memberId}/reservation/{reservationId}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Integer memberId, @PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }
    // 예매 생성(테스트 완료)
    @PostMapping("/member/{memberId}")
    public ResponseEntity<ReservationDTO> createReservation(@PathVariable Integer memberId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(memberId, reservationDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }
}
