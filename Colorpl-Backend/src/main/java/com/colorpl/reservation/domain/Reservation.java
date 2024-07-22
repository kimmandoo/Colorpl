package com.colorpl.reservation.domain;

import com.colorpl.global.common.BaseEntity;
import com.colorpl.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
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
    @GeneratedValue
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
    private List<ReservationDetail> reservationDetails;

    @Column(name = "RESERVE_COMMENT")
    private String comment;

    @Column(name = "IS_REFUNDED")
    private boolean isRefunded;


}
