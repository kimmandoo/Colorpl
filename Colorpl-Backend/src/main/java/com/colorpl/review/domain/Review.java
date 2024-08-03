package com.colorpl.review.domain;

import com.colorpl.comment.domain.Comment;
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
public class Review {

    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "REVIEW_CONTENT")
    private  String content;

    @Column(name = "IS_SPOILER")
    private  Boolean spoiler;

    @Column(name = "REVIEW_EMOTION")
    private  Byte emotion;

    @Column(name = "EMPHATHY_NUMBER")
    private  Integer emphathy;

    // ?
    @OneToMany(mappedBy = "review", cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "TICKET_ID")
    private Ticket ticket;

//    public void setTicket(Ticket ticket) {
//        this.ticket = ticket;
//    }

    public void updateReview(String content, Boolean spoiler, Byte emotion) {
        this.content = content;
        this.spoiler = spoiler;
        this.emotion = emotion;
    }

}
