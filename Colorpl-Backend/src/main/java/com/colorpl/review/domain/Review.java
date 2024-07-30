package com.colorpl.review.domain;

import com.colorpl.comment.domain.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
//@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {

    @Column(name = "REVIEW_ID")
    @GeneratedValue
    @Id
    private Integer id;

    // schdule 아직 없음
    @JoinColumn(name = "SCHEDULE_ID")
    @OneToOne(mappedBy = "SCHEDULE_ID")
    private Integer schedule;

    @Column(name = "REVIEW_CONTENT")
    private  String content;

    @Column(name = "IS_SPOILER")
    private  Boolean spoiler;

    @Column(name = "REVIEW_EMOTION")
    private  Integer emotion;

    @Column(name = "EMPHATHY_NUMBER")
    private  Byte emphathy;

    // ?
    @OneToMany(mappedBy = "review", cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void updateReview(Integer schedule, String content, Boolean spoiler, Integer emotion) {
        this.schedule = schedule;
        this.content = content;
        this.spoiler = spoiler;
        this.emotion = emotion;
    }

}
