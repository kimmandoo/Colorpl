package com.colorpl.comment.dto;

import com.colorpl.member.dto.MemberDTO;
import com.colorpl.review.dto.ReviewDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private ReviewDTO review;
    private MemberDTO member;
    private String content;
}