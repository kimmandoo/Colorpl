package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RequestDTO {
    private Integer memberId;
    private Long ticketId;
    private String content;
    private Boolean spoiler;
    private Byte emotion;

}



