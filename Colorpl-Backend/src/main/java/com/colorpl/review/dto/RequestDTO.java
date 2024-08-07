package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RequestDTO {
//    private Integer memberId;
    private Long scheduleId;
    private String content;
    private Boolean spoiler;
    // to fix swagger error
    @Schema(implementation = Integer.class)
    private Byte emotion;

}



