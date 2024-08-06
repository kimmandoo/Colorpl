package com.colorpl.show.query.dao;

import com.colorpl.show.domain.detail.Category;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ShowDetailSearchCondition {

    private LocalDate date;
    private String area;
    private String keyword;
    private Category category;
}
