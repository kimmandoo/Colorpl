package com.colorpl.theater.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater {

    @Column(name = "THEATER_ID")
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "THEATER_API_ID")
    private String apiId;

    @Column(name = "THEATER_NAME")
    private String name;

    @Column(name = "THEATER_ADDRESS")
    private String address;

    @Column(name = "THEATER_LATITUDE")
    private Double latitude;

    @Column(name = "THEATER_LONGITUDE")
    private Double longitude;
}
