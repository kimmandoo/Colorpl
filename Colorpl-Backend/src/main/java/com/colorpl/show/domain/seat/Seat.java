package com.colorpl.show.domain.seat;

import com.colorpl.show.domain.schedule.ShowSchedule;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Column(name = "SEAT_ID")
    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "SHOW_SCHEDULE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ShowSchedule showSchedule;

    @Column(name = "SEAT_ROW")
    private Byte row;

    @Column(name = "SEAT_COL")
    private Byte col;

    @Column(name = "SEAT_LEVEL")
    private String level;

}
