package com.colorpl.reservation.domain;

import com.colorpl.global.common.BaseEntity;
import com.colorpl.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVE_ID")
    private Long id;

    //사용자 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "RESERVE_DATE")
    private LocalDateTime date;

    @Column(name = "RESERVE_AMOUNT")
    private String amount;


    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReservationDetail> reservationDetails = new ArrayList<>();

    @Column(name = "RESERVE_COMMENT")
    private String comment;

    @Column(name = "IS_REFUNDED")
    private boolean isRefunded;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateRefundState(boolean refunded) {
        isRefunded = refunded;
    }

    //연관관계 편의 메서드
    public void addReservationDetail(ReservationDetail reservationDetail) {

        if (reservationDetails.contains(reservationDetail)) {
            throw new IllegalArgumentException("이미 추가된 항목입니다.");
        }
        this.reservationDetails.add(reservationDetail);
        reservationDetail.updateReservation(this);
    }

    public void removeReservationDetail(ReservationDetail reservationDetail) {
        if (!reservationDetails.contains(reservationDetail)) {
            throw new IllegalArgumentException("삭제할 항목이 없습니다.");
        }
        this.reservationDetails.remove(reservationDetail);
        reservationDetail.updateReservation(null);
    }

    public void updateReservation(LocalDateTime date, String amount, String comment, boolean refunded) {
        this.date = date;
        this.amount = amount;
        this.comment = comment;
        isRefunded = refunded;
    }

}
