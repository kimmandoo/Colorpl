package com.colorpl.schedule.ui;

import com.colorpl.show.domain.detail.Category;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateCustomScheduleRequest {

    private String seat;
    private LocalDateTime dateTime;
    private String name;
    private Category category;
    private String location;
    private Double latitude;
    private Double longitude;
}
