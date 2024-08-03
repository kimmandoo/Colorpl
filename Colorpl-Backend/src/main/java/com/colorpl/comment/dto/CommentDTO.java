package com.colorpl.comment.dto;

import com.colorpl.comment.domain.Comment;
import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private Long reviewId;
    private Integer memberId; // Changed to Long for consistency
    private String commentContent;

    // Static method to create a CommentDTO from a Comment entity
    public static CommentDTO fromComment(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .reviewId(comment.getReview() != null ? comment.getReview().getId() : null)
                .memberId(comment.getMember() != null ? comment.getMember().getId() : null)
                .commentContent(comment.getComment_content())
                .build();
    }

    // Static method to create a CommentDTO from a Comment entity
    public static CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .reviewId(comment.getReview() != null ? comment.getReview().getId() : null)
                .memberId(comment.getMember() != null ? comment.getMember().getId() : null)
                .commentContent(comment.getComment_content())
                .build();
    }

    public Comment toComment(Member member, Review review) {
        return Comment.builder()
                .id(this.id)
                .member(member)
                .review(review)
                .comment_content(this.commentContent)
                .build();
    }
}
