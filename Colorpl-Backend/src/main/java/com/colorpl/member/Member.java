package com.colorpl.member;

import com.colorpl.global.common.BaseEntity;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.schedule.domain.Schedule;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

    private String password;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    // 연관관계 편의 메서드
    public void addReservation(Reservation reservation) {
        if (reservations.contains(reservation)) {
            throw new IllegalArgumentException("이미 추가된 예매입니다.");
        }
        reservations.add(reservation);
        reservation.updateMember(this);
    }

    public void removeReservation(Reservation reservation) {
        if (!reservations.contains(reservation)) {
            throw new IllegalArgumentException("삭제할 예매가 없습니다.");
        }
        reservations.remove(reservation);
        reservation.updateMember(null);
    }

}