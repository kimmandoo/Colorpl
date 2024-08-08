package com.colorpl.show.repository;

import static com.colorpl.show.domain.QShowSchedule.showSchedule;

import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.domain.ShowSchedule;
import com.colorpl.show.dto.SearchShowScheduleCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShowScheduleRepositoryImpl implements ShowScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ShowSchedule> search(SearchShowScheduleCondition condition) {
        return queryFactory
            .select(showSchedule)
            .from(showSchedule)
            .where(
                showDetailEq(condition.getShowDetail()),
                dateTimeBetween(condition.getFrom(), condition.getTo())
            )
            .fetch();
    }

    private BooleanExpression showDetailEq(ShowDetail showDetail) {
        return showSchedule.showDetail.eq(showDetail);
    }

    private BooleanExpression dateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return showSchedule.dateTime.between(from, to);
    }
}
