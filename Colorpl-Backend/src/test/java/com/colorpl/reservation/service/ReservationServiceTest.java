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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ShowScheduleRepository showScheduleRepository;

    @Test
    void testGetReservationsByMemberId() {
        Integer memberId = 1;
        List<Reservation> reservations = new ArrayList<>();
        Member member = Member.builder()
            .id(memberId)
            .build();
        Reservation reservation = Reservation.builder()
            .id(1L)
            .member(member) // Ensure Member is set
            .build();
        reservations.add(reservation);

        when(reservationRepository.findByMemberId(memberId)).thenReturn(reservations);

        List<ReservationDTO> result = reservationService.getReservationsByMemberId(memberId);

        verify(reservationRepository, times(1)).findByMemberId(memberId);
        assertEquals(1, result.size());
        assertThat(result.get(0).getId()).isEqualTo(reservation.getId());
    }

    @Test
    void testDeleteAllReservationsByMemberId() {
        Integer memberId = 1;
        Member member = Member.builder()
            .id(memberId)
            .reservations(new ArrayList<>()) // Initialize the reservations list
            .build();

        Reservation reservation = Reservation.builder()
            .id(1L)
            .member(member)
            .build();

        member.getReservations().add(reservation);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(reservationRepository.findByMemberId(memberId)).thenReturn(List.of(reservation));

        reservationService.deleteAllReservationsByMemberId(memberId);

        verify(memberRepository, times(1)).findById(memberId);
        verify(reservationRepository, times(1)).findByMemberId(memberId);
        verify(reservationRepository, times(1)).deleteAll(List.of(reservation));
        assertThat(member.getReservations()).isEmpty();
    }

    @Test
    void testCancelReservationById() {
        Long reservationId = 1L;
        Reservation reservation = Reservation.builder()
            .id(reservationId)
            .isRefunded(false)
            .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservationById(reservationId);

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertTrue(reservation.isRefunded());
    }

    @Test
    void testUpdateReservation() {
        Integer memberId = 1;
        Long reservationId = 1L;
        ReservationDTO reservationDTO = ReservationDTO.builder()
            .date(LocalDateTime.now())
            .amount("100")
            .comment("Updated")
            .isRefunded(false)
            .reservationDetails(List.of(ReservationDetailDTO.builder().id(null).showScheduleId(1).build()))
            .build();

        Member member = Member.builder()
            .id(memberId)
            .build();

        Reservation reservation = Reservation.builder()
            .id(reservationId)
            .member(member) // Ensure Member is set
            .reservationDetails(new ArrayList<>())
            .build();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(showScheduleRepository.findById(anyInt())).thenReturn(Optional.of(ShowSchedule.builder().id(1).build()));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDTO updatedReservation = reservationService.updateReservation(memberId, reservationId, reservationDTO);

        verify(reservationRepository, times(1)).findById(reservationId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertThat(updatedReservation.getAmount()).isEqualTo(reservationDTO.getAmount());
        assertThat(updatedReservation.getComment()).isEqualTo(reservationDTO.getComment());
    }

    @Test
    void testCreateReservation() {
        Integer memberId = 1;
        ReservationDTO reservationDTO = ReservationDTO.builder()
            .date(LocalDateTime.now())
            .amount("100")
            .comment("New Reservation")
            .isRefunded(false)
            .reservationDetails(List.of(ReservationDetailDTO.builder().id(null).showScheduleId(1).build()))
            .build();

        Member member = Member.builder()
            .id(memberId)
            .reservations(new ArrayList<>()) // Initialize the reservations list
            .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(showScheduleRepository.findById(anyInt())).thenReturn(Optional.of(ShowSchedule.builder().id(1).build()));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDTO newReservation = reservationService.createReservation(memberId, reservationDTO);

        verify(memberRepository, times(1)).findById(memberId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertThat(newReservation.getComment()).isEqualTo(reservationDTO.getComment());
        assertThat(newReservation.getAmount()).isEqualTo(reservationDTO.getAmount());
        assertThat(newReservation.getReservationDetails()).hasSize(1);
    }
}
