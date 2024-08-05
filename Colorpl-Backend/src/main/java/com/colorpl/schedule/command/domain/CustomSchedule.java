package com.colorpl.schedule.command.domain;

import com.colorpl.show.domain.detail.Category;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@DiscriminatorValue("CUSTOM")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CustomSchedule extends Schedule {

    @Column(name = "SCHEDULE_SEAT")
    private String seat;

    @Column(name = "SCHEDULE_DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "SCHEDULE_NAME")
    private String name;

    @Column(name = "SCHEDULE_CATEGORY")
    private Category category;

    @Column(name = "SCHEDULE_LOCATION")
    private String location;

    @Column(name = "SCHEDULE_LATITUDE")
    private Double latitude;

    @Column(name = "SCHEDULE_LONGITUDE")
    private Double longitude;
}
