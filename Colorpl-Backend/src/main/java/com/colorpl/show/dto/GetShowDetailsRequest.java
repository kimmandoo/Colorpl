package com.colorpl.show.dto;

import com.colorpl.show.domain.Area;
import com.colorpl.show.domain.Category;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetShowDetailsRequest {

    private LocalDate date;
    private Area area;
    private String keyword;
    private Category category;
}
