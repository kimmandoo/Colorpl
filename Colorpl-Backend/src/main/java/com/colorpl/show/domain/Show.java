package com.colorpl.show.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show {

    @Column(name = "SHOW_ID")
    @GeneratedValue
    @Id
    private Integer id;

    @CollectionTable(name = "PRICE_BY_SEAT_CLASS", joinColumns = @JoinColumn(name = "SHOW_ID"))
    @Column(name = "PBSC_PRICE")
    @ElementCollection
    @MapKeyColumn(name = "PBSC_SEAT_CLASS")
    private Map<String, Integer> priceBySeatClass;

    @Column(name = "SHOW_NAME")
    private String name;

    @Column(name = "SHOW_START_DATE")
    private LocalDateTime startDate;

    @Column(name = "SHOW_END_DATE")
    private LocalDateTime endDate;

    @Column(name = "SHOW_CAST")
    private String cast;

    @Column(name = "SHOW_RUNTIME")
    private String runtime;

    @Column(name = "SHOW_RATING")
    private String rating;

    @Column(name = "SHOW_POSTER_IMAGE_PATH")
    private String posterImagePath;

    @Column(name = "SHOW_AREA")
    private String area;

    @Column(name = "SHOW_GENRE")
    private String genre;

    @Column(name = "SHOW_STATE")
    @Enumerated(EnumType.STRING)
    private ShowState state;

    @Column(name = "SHOW_SCHEDULE")
    private String schedule;

}
