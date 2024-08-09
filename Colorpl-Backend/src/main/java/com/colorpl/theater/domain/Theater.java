package com.colorpl.theater.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THEATER_ID")
    private Integer id;

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

    @OneToMany(mappedBy = "theater")
    private List<Hall> halls = new ArrayList<>();
}
