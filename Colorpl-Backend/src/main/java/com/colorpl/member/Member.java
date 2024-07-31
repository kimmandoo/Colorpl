package com.colorpl.member;

import com.colorpl.global.common.BaseEntity;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.review.domain.Review;
import com.colorpl.show.domain.detail.Category;
import com.colorpl.ticket.domain.Ticket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseEntity{

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Category> categories=new HashSet<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)  // 리뷰와 연관된 코드 추가
//    private List<Review> reviews;

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
    public void addTicket(Ticket ticket) {
        if (tickets.contains(ticket)) {
            throw new IllegalArgumentException("이미 추가된 티켓입니다.");
        }
        tickets.add(ticket);
        ticket.updateMember(this);
    }

    public void removeTicket(Ticket ticket) {
        if (!tickets.contains(ticket)) {
            throw new IllegalArgumentException("삭제할 티켓이 없습니다.");
        }
        tickets.remove(ticket);
        ticket.updateMember(null);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public static Member toMember(MemberDTO memberDTO, PasswordEncoder encoder) {
        return Member.builder()
            .email(memberDTO.getEmail())
            .password(encoder.encode(memberDTO.getPassword()))
            .nickname(memberDTO.getNickname())
            .type(MemberType.USER)  // 기본 값으로 USER 설정
            .build();
    }

    public void updateMember(Member member, PasswordEncoder encoder) {
        this.email = member.getEmail();
        this.password = encoder.encode(member.getPassword());
        this.nickname = member.getNickname();
        this.type = member.getType();
    }


    public String getUsername() {
        return email;
    }
}
