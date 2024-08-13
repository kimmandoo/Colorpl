package com.colorpl.show.repository;

import com.colorpl.show.domain.Seat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeatRepository {

    private final JdbcTemplate jdbcTemplate;
    @Value("${insert.into.seat}")
    private String sql;

    public void batchInsert(List<Seat> seats) {
        jdbcTemplate.batchUpdate(sql, seats, seats.size(), (ps, argument) -> {
            ps.setInt(1, argument.getShowDetail().getId());
            ps.setInt(2, argument.getRow());
            ps.setInt(3, argument.getCol());
            ps.setString(4, argument.getSeatClass().toString());
        });
    }
}
