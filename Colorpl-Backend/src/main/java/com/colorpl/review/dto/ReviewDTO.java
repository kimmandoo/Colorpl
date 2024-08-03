package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import com.colorpl.ticket.domain.Ticket;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class ReviewDTO {
    private Long id;
    private Long ticketId;
    private String content;
    private Boolean spoiler;
    private Byte emotion;
    private Integer empathy;
    @Builder.Default
    private List<CommentDTO> comments = new ArrayList<>(); // Initialize to empty list


    public static ReviewDTO toReviewDTO(Review review) {
        List<CommentDTO> commentDTOs = review.getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());

        return ReviewDTO.builder()
                .id(review.getId())
                .ticketId(review.getTicket() != null ? review.getTicket().getId() : null)
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .empathy(review.getEmphathy())
                .comments(commentDTOs)
                .build();
    }

}