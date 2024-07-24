package com.colorpl.reservation.controller;

import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 특정 멤버의 모든 예약 조회(테스트 완료)
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByMemberId(@PathVariable Integer memberId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByMemberId(memberId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // 모든 예약 삭제(테스트 완료)
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteAllReservationsByMemberId(@PathVariable Integer memberId) {
        reservationService.deleteAllReservationsByMemberId(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 특정 예약 취소(테스트 완료)
    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<Void> cancelReservationById(@PathVariable Long reservationId) {
        reservationService.cancelReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 멤버의 특정 예약 취소(테스트 완료)
    @PostMapping("/cancel/member/{memberId}/reservation/{reservationId}")
    public ResponseEntity<Void> cancelReservationByMemberIdAndReservationId(@PathVariable Integer memberId, @PathVariable Long reservationId) {
        reservationService.cancelReservationByMemberIdAndReservationId(memberId, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    // 특정 예약 업데이트
//    @PutMapping("/member/{memberId}/reservation/{reservationId}")
//    public ResponseEntity<Reservation> updateReservation(@PathVariable Integer memberId, @PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
//        Reservation updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);
//        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
//    }
//
//    // 예약 생성
//    @PostMapping("/member/{memberId}")
//    public ResponseEntity<Reservation> createReservation(@PathVariable Integer memberId, @RequestBody ReservationDTO reservationDTO) {
//        Reservation createdReservation = reservationService.createReservation(memberId, reservationDTO);
//        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
//    }
    // 특정 예약 업데이트(테스트 완료)
    @PutMapping("/member/{memberId}/reservation/{reservationId}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Integer memberId, @PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);
        return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
    }
    // 예약 생성(테스트 완료)
    @PostMapping("/member/{memberId}")
    public ResponseEntity<ReservationDTO> createReservation(@PathVariable Integer memberId, @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(memberId, reservationDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }
}
