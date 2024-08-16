package com.colorpl.theater.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HALL_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THEATER_ID")
    private Theater theater;

    @Column(name = "HALL_NAME")
    private String name;

    @Column(name = "HALL_API_ID")
    private String apiId;

    @Builder
    public Hall(Theater theater, String name, String apiId) {
        this.theater = theater;
        this.name = name;
        this.apiId = apiId;

        theater.getHalls().add(this);
    }
}
