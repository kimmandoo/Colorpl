package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewDTO {
    private Integer id;
    private String content;
    private Boolean spoiler;
    private Integer emotion;
    private Byte empathy;
    private List<CommentDTO> comments;

    public Review orElseThrow(Object o) {
        return null;
    }
}