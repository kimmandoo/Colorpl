package com.colorpl.show.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ShowSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOW_SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_DETAIL_ID")
    private ShowDetail showDetail;

    @Column(name = "SHOW_SCHEDULE_DATE_TIME")
    private LocalDateTime dateTime;

    @Builder
    public ShowSchedule(LocalDateTime dateTime, Long id, ShowDetail showDetail) {
        this.dateTime = dateTime;
        this.id = id;
        this.showDetail = showDetail;
        showDetail.getShowSchedules().add(this);
    }
}
