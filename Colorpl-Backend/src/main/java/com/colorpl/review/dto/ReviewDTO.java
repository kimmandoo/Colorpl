package com.colorpl.review.dto;

import com.colorpl.comment.domain.Comment;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewDTO {
    //    private Integer id;
    private String content;
    private Boolean spoiler;
    private Integer emotion;
    private Byte empathy;
    private List<Comment> comments;


    public static ReviewDTO toReviewDTO(Review review) {
        return ReviewDTO.builder()
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .empathy(review.getEmphathy())
//                .comments(review.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList()))
                .comments(null)
                .build();
    }
}