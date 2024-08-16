package com.colorpl.schedule.dto;

import lombok.Getter;

@Getter
public class CreateCustomScheduleRequest {

    private String seat;
    private String dateTime;
    private String name;
    private String category;
    private String location;
    private Double latitude;
    private Double longitude;
}
