package com.colorpl.schedule.domain;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Review;
import com.colorpl.show.domain.Category;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("C")
@Entity
public class CustomSchedule extends Schedule {

    @Column(name = "SCHEDULE_SEAT")
    private String seat;

    @Column(name = "SCHEDULE_DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "SCHEDULE_NAME")
    private String name;

    @Column(name = "SCHEDULE_CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "SCHEDULE_LOCATION")
    private String location;

    @Column(name = "SCHEDULE_LATITUDE")
    private Double latitude;

    @Column(name = "SCHEDULE_LONGITUDE")
    private Double longitude;

    @Builder
    private CustomSchedule(
        Long id,
        Member member,
        String image,
        Review review,
        String seat,
        LocalDateTime dateTime,
        String name,
        Category category,
        String location,
        Double latitude,
        Double longitude
    ) {
        super(id, member, image, review);
        this.seat = seat;
        this.dateTime = dateTime;
        this.name = name;
        this.category = category;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;

        member.getSchedules().add(this);
    }
}
