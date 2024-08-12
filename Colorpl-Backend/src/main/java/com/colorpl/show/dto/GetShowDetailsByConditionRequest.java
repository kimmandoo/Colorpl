package com.colorpl.show.dto;

import com.colorpl.show.domain.Category;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetShowDetailsByConditionRequest {

    private LocalDate date;
    private String area;
    private String keyword;
    private Category category;
}
