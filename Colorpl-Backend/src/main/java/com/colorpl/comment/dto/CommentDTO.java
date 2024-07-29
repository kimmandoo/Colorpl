package com.colorpl.comment.dto;

import com.colorpl.comment.domain.Comment;
import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Review review;
    private Member member;
    private String comment_content;

    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .member(comment.getMember())
                .review(comment.getReview())
                .comment_content(comment.getComment_content())
                .build();
    }

}
