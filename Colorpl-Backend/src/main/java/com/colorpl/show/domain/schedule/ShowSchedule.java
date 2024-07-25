package com.colorpl.show.domain.schedule;

import com.colorpl.reservation.domain.ReservationDetail;
import com.colorpl.show.domain.Show;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowSchedule {

    @Column(name = "SHOW_SCHEDULE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @JoinColumn(name = "SHOW_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @Column(name = "SHOW_SCHEDULE_DATE")
    private LocalDateTime date;

    @OneToMany(mappedBy = "showSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationDetail> reservationDetails;

}
