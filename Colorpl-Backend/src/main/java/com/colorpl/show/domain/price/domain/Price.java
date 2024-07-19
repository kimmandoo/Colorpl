package com.colorpl.show.domain.price.domain;

import com.colorpl.show.domain.Show;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PRICE_BY_SEAT_CLASS")
public class Price {

    @Column(name = "PBSC_ID")
    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "SHOW_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @Column(name = "PBSC_SEAT_CLASS")
    private String seatClass;

    @Column(name = "PBSC_PRICE")
    private Integer price;

}
