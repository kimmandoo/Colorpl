package com.colorpl.member.repository;

import com.colorpl.member.Member;

import com.colorpl.member.dto.MemberDTO;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.domain.ReservationDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmail(String email);
    List<Member> findByNickname(String nickname);

}

