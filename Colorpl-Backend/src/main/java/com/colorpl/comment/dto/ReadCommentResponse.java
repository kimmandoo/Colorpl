package com.colorpl.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@Builder
public class ReadCommentResponse {
    private List<CommentDTO> items;
    private int totalPage;

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
