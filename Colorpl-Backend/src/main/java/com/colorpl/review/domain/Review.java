package com.colorpl.review.domain;

import com.colorpl.comment.domain.Comment;
import com.colorpl.global.common.BaseEntity;
import com.colorpl.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
//@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "REVIEW_FILENAME")
    private String filename;

    @Column(name = "REVIEW_CONTENT")
    private  String content;

    @Column(name = "IS_SPOILER")
    private  Boolean spoiler;

    @Column(name = "REVIEW_EMOTION")
    private  Byte emotion;

    @Column(name = "EMPHATHY_NUMBER")
    @Builder.Default
    private  Integer emphathy = 0;

    @OneToMany(mappedBy = "review", cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "TICKET_ID")
    private Ticket ticket;
    
    // 공감 유저 추적
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Empathy> empathies = new ArrayList<>();

    public void updateReview(String content, Boolean spoiler, Byte emotion) {
        this.content = content;
        this.spoiler = spoiler;
        this.emotion = emotion;
    }

    // 공감 수 증가
    public void incrementEmphathy() {
        this.emphathy = (this.emphathy == null) ? 1 : this.emphathy + 1;
    }

    // 공감 수 감소
    public void decrementEmphathy() {
        if (this.emphathy != null && this.emphathy > 0) {
            this.emphathy = this.emphathy - 1;
        }
    }

}
