package com.colorpl.reservation.service;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.reservation.dto.ReservationDetailDTO;
import com.colorpl.reservation.repository.ReservationRepository;
import com.colorpl.show.domain.schedule.ShowSchedule;
import com.colorpl.show.repository.ShowScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testUpdateReservationDetail() {
        // Arrange
        Member member = Member.builder()
            .email("test@example.com")
            .nickname("tester")
            .password("password")
            .build();

        ShowSchedule showSchedule = ShowSchedule.builder()
            .date(LocalDateTime.now())
            .build();

        ReservationDetail reservationDetail = ReservationDetail.builder()
            .row((byte) 1)
            .col((byte) 1)
            .showSchedule(showSchedule)
            .build();

        Reservation reservation = Reservation.builder()
            .member(member)
            .date(LocalDateTime.now())
            .amount("100")
            .comment("Test Comment")
            .isRefunded(false)
            .reservationDetails(Collections.singletonList(reservationDetail))
            .build();

        ReservationDetailDTO detailDTO = ReservationDetailDTO.builder()
            .row((byte) 2)
            .col((byte) 2)
            .showScheduleId(showSchedule.getId())
            .build();

        when(reservationRepository.findDetailByIdAndMemberId(reservationDetail.getId(), member.getId()))
            .thenReturn(Optional.of(reservationDetail));
        when(showScheduleRepository.findById(detailDTO.getShowScheduleId()))
            .thenReturn(Optional.of(showSchedule));

        // Act
        reservationService.updateReservationDetail(member.getId(), reservationDetail.getId(), detailDTO);

        // Assert
        assertEquals((byte) 2, reservationDetail.getRow());
        assertEquals((byte) 2, reservationDetail.getCol());
    }

    @Test
    void testAddReservation() {
        // Arrange
        Member member = Member.builder()
            .email("test@example.com")
            .nickname("tester")
            .password("password")
            .build();

        ShowSchedule showSchedule = ShowSchedule.builder()
            .date(LocalDateTime.now())
            .build();

        ReservationDetailDTO detailDTO = ReservationDetailDTO.builder()
            .row((byte) 1)
            .col((byte) 1)
            .showScheduleId(showSchedule.getId())
            .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
            .date(LocalDateTime.now())
            .amount("200")
            .comment("New Comment")
            .isRefunded(false)
            .reservationDetails(Collections.singletonList(detailDTO))
            .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(showScheduleRepository.findById(detailDTO.getShowScheduleId()))
            .thenReturn(Optional.of(showSchedule));

        // Act
        reservationService.addReservation(member.getId(), reservationDTO);

        // Assert
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
}
