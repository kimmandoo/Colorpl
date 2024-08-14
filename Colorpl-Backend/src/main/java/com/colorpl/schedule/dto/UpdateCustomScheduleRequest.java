package com.colorpl.schedule.dto;

import com.colorpl.show.domain.Category;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateCustomScheduleRequest {

    private String seat;
    private String dateTime;
    private String name;
    private Category category;
    private String location;
    private Double latitude;
    private Double longitude;
}
