package com.colorpl.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class CommentResponse {
    private Long commentId;
    private String failPoint;
}



//if (commentDTO.getMemberId() == null) {
//
//CommentResponse response = CommentResponse.builder()
//        .commentId(null)
//        .failPoint("Member ID는 필수입니다")
//        .build();
//            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);