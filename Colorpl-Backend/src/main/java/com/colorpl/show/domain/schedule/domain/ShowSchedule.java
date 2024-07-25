package com.colorpl.show.domain.schedule.domain;

import com.colorpl.show.domain.show.domain.Show;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowSchedule {

    @Column(name = "SHOW_SCHEDULE_ID")
    @GeneratedValue
    @Id
    private Integer id;

    @JoinColumn(name = "SHOW_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @Column(name = "SHOW_SCHEDULE_DATE")
    private LocalDateTime date;

}
