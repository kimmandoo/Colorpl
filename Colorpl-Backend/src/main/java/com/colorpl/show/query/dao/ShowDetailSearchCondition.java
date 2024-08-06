package com.colorpl.show.query.dao;

import com.colorpl.show.domain.detail.Category;
import java.time.LocalDate;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class ShowDetailSearchCondition {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String area;
    private String keyword;
    private Category category;

}
