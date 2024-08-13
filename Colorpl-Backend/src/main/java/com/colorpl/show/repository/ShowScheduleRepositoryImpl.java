package com.colorpl.show.repository;

import com.colorpl.show.domain.ShowSchedule;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowScheduleRepositoryImpl implements ShowScheduleRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;
    @Value("${insert.into.show-schedule}")
    private String sql;

    public void batchInsert(List<ShowSchedule> showSchedules) {
        jdbcTemplate.batchUpdate(sql, showSchedules, showSchedules.size(), (ps, argument) -> {
            Instant instant = argument.getDateTime().atZone(ZoneId.systemDefault()).toInstant();
            ps.setTimestamp(1, Timestamp.from(instant));
            ps.setInt(2, argument.getShowDetail().getId());
        });
    }
}
