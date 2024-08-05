package com.colorpl.review.domain;

import com.colorpl.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Empathy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPATHY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID", referencedColumnName = "REVIEW_ID")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
    private Member member;

    private Boolean myempathy = true;

    public void deactivate() {
        this.myempathy = false;
    }

    public void activate() {
        this.myempathy = true;
    }
}

