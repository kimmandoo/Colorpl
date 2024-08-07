package com.colorpl.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class FailCommentResponse {
    private Long commentId;
    private String failPoint;
}
