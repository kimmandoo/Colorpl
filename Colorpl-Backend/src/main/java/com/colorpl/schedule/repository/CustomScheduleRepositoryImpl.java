package com.colorpl.schedule.repository;

import static com.colorpl.schedule.domain.QCustomSchedule.customSchedule;

import com.colorpl.member.Member;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CustomSchedule> search(SearchScheduleCondition condition) {
        return queryFactory
            .select(customSchedule)
            .from(customSchedule)
            .where(
                memberEq(condition.getMember()),
                dateTimeBetween(condition.getFrom(), condition.getTo())
            )
            .fetch();
    }

    private BooleanExpression memberEq(Member member) {
        return member != null ? customSchedule.member.eq(member) : null;
    }

    private BooleanExpression dateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return from != null && to != null ? customSchedule.dateTime.between(from, to) : null;
    }
}
