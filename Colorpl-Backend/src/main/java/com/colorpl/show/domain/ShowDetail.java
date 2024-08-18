package com.colorpl.show.domain;

import com.colorpl.theater.domain.Hall;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity(name = "SHOW_DETAIL")
public class ShowDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOW_DETAIL_ID")
    private Integer id;

    @Column(name = "SHOW_DETAIL_API_ID")
    private String apiId;

    @Column(name = "SHOW_DETAIL_NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HALL_ID")
    private Hall hall;

    @Column(name = "SHOW_DETAIL_CAST")
    private String cast;

    @Column(name = "SHOW_DETAIL_RUNTIME")
    private String runtime;

    @ElementCollection
    @CollectionTable(name = "PRICE_BY_SEAT_CLASS", joinColumns = @JoinColumn(name = "SHOW_DETAIL_ID"))
    @MapKeyColumn(name = "PRICE_BY_SEAT_CLASS_SEAT_CLASS")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "PRICE_BY_SEAT_CLASS_PRICE")
    private Map<SeatClass, Integer> priceBySeatClass;

    @Column(name = "SHOW_DETAIL_POSTER_IMAGE_PATH")
    private String posterImagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_DETAIL_AREA")
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_DETAIL_CATEGORY")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHOW_DETAIL_STATE")
    private ShowState state;

    @OneToMany(mappedBy = "showDetail")
    @Builder.Default
    private List<ShowSchedule> showSchedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "showDetail")
    private List<Seat> seats = new ArrayList<>();
}
