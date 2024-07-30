package com.colorpl.show.domain.schedule;

import com.colorpl.show.domain.detail.ShowDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowScheduleRepository extends JpaRepository<ShowSchedule, Long> {

    List<ShowSchedule> findByShowDetail(ShowDetail showDetail);
}
