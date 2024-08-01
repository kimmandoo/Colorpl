package com.colorpl.schedule.domain;

import com.colorpl.review.domain.Review;
import jakarta.persistence.*;

public class Schedule {
    @Id
    @GeneratedValue
    @Column(name = "SCHDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Review review;
}