package com.colorpl.show.query.dao;

import static com.colorpl.show.domain.detail.QShowDetail.showDetail;
import static com.colorpl.show.domain.schedule.QShowSchedule.showSchedule;

import com.colorpl.show.domain.detail.Category;
import com.colorpl.show.domain.detail.ShowDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowDetailRepositoryImpl implements ShowDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ShowDetail> showList() {
        return queryFactory.select(showDetail).from(showDetail).where().fetch();
    }

    @Override
    public List<ShowDetail> search(ShowDetailSearchCondition condition) {

        LocalDateTime from = condition.getDate().atStartOfDay();
        LocalDateTime to = from.plusDays(1);

        return queryFactory
            .select(showDetail).distinct()
            .from(showSchedule)
            .join(showSchedule.showDetail, showDetail).fetchJoin()
            .where(
                dateTimeBetween(from, to),
                areaEq(condition.getArea()),
                nameContains(condition.getKeyword()),
                categoryEq(condition.getCategory())
            )
            .fetch();
    }

    private BooleanExpression dateTimeBetween(LocalDateTime from, LocalDateTime to) {
        return from != null && to != null ? showSchedule.dateTime.between(from, to) : null;
    }

    private BooleanExpression areaEq(String area) {
        return area != null ? showDetail.area.eq(area) : null;
    }

    private BooleanExpression nameContains(String keyword) {
        return keyword != null ? showDetail.name.contains(keyword) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? showDetail.category.eq(category) : null;
    }
}
