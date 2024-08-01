package com.colorpl.review.domain;

import com.colorpl.comment.domain.Comment;
import com.colorpl.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // schedule 아직 없음
    @OneToOne(mappedBy = "review")
    private Schedule schedule;

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

    public void updateReview(String content, Boolean spoiler, Byte emotion) {
        this.content = content;
        this.spoiler = spoiler;
        this.emotion = emotion;
    }

}
