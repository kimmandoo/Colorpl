package com.colorpl.show.dto;

import com.colorpl.show.domain.detail.Category;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchShowDetailCondition {

    private LocalDate date;
    private String area;
    private String keyword;
    private Category category;
}
