package com.colorpl.show.domain.detail;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import java.util.Map;
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
public class ShowDetail {

    @Column(name = "SHOW_DETAIL_ID")
    @GeneratedValue
    @Id
    private Integer id;

    @Column(name = "SHOW_DETAIL_API_ID")
    private String apiId;

    @Column(name = "SHOW_DETAIL_NAME")
    private String name;

    @Column(name = "SHOW_DETAIL_CAST")
    private String cast;

    @Column(name = "SHOW_DETAIL_RUNTIME")
    private String runtime;

    @CollectionTable(name = "PRICE_BY_SEAT_CLASS", joinColumns = @JoinColumn(name = "SHOW_DETAIL_ID"))
    @Column(name = "PRICE_BY_SEAT_CLASS_PRICE")
    @ElementCollection
    @MapKeyColumn(name = "PRICE_BY_SEAT_CLASS_SEAT_CLASS")
    private Map<String, Integer> priceBySeatClass;

    @Column(name = "SHOW_DETAIL_POSTER_IMAGE_PATH")
    private String posterImagePath;

    @Column(name = "SHOW_DETAIL_AREA")
    private String area;

    @Column(name = "SHOW_DETAIL_STATE")
    @Enumerated(EnumType.STRING)
    private ShowState state;
}
