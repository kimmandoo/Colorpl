package com.colorpl.review.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@Builder
public class ReadReviewResponse {
    private List<ReviewDTO> items;
    private int totalPage;

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
