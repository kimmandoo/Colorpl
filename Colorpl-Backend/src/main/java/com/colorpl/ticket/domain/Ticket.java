package com.colorpl.ticket.domain;

import static jakarta.persistence.FetchType.*;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import com.colorpl.show.domain.detail.Category;
import jakarta.persistence.*;

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

    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "REVIEW_ID")
    private Review review;


}
