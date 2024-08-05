package com.colorpl.ticket.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.colorpl.member.Member;
import com.colorpl.show.domain.detail.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
public class Ticket {

    @Column(name = "TICKET_ID")
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "TICKET_NAME")
    private String name;

    @Column(name = "TICKET_THEATER")
    private String theater;

    @Column(name = "TICKET_FILENAME")
    private String filename;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void updateMember(Member member) {
        this.member = member;
    }

    @Column(name = "TICKET_DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "TICKET_SEAT")
    private String seat;

    @Column(name = "TICKET_CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "TICKET_LATITUDE")
    private Double latitude;

    @Column(name = "TICKET_LONGITUDE")
    private Double longitude;
}
