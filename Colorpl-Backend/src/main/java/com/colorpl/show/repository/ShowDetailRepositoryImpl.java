package com.colorpl.show.repository;

import static com.colorpl.show.domain.QShowDetail.showDetail;
import static com.colorpl.show.domain.QShowSchedule.showSchedule;
import static com.colorpl.theater.domain.QHall.hall;
import static com.colorpl.theater.domain.QTheater.theater;

import com.colorpl.show.domain.Area;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowSchedulesRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowDetailRepositoryImpl implements ShowDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ShowDetail> getShowsByCondition(LocalDate date, String keyword, List<Area> area,
        Category category, Integer cursorId, Long limit) {
        return queryFactory
            .select(showDetail).distinct()
            .from(showDetail)
            .join(showDetail.priceBySeatClass).fetchJoin()
            .join(showDetail.showSchedules, showSchedule).fetchJoin()
            .where(
                dateEq(date),
                nameContains(keyword),
                areaIn(area),
                categoryEq(category),
                cursorIdGt(cursorId)
            )
            .limit(limit)
            .fetch();
    }

    @Override
    public ShowDetail getShowDetail(Integer showDetailId) {
        return queryFactory
            .select(showDetail)
            .from(showDetail)
            .join(showDetail.showSchedules, showSchedule).fetchJoin()
            .where(showDetail.id.eq(showDetailId))
            .fetchOne();
    }

    public ShowDetail getShowSchedules(GetShowSchedulesRequest condition) {
        return queryFactory
            .select(showDetail).distinct()
            .from(showDetail)
            .join(showDetail.hall, hall).fetchJoin()
            .join(hall.theater, theater).fetchJoin()
            .join(showDetail.showSchedules, showSchedule).fetchJoin()
            .where(
                showDetailIdEq(condition.getShowDetailId()),
                dateEq(condition.getDate())
            )
            .fetchOne();
    }

    private BooleanExpression dateEq(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = from.plusDays(1);
        return showSchedule.dateTime.between(from, to);
    }

    private BooleanExpression nameContains(String keyword) {
        return keyword != null ? showDetail.name.contains(keyword) : null;
    }

    private BooleanExpression areaIn(List<Area> area) {
        return area != null ? showDetail.area.in(area) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? showDetail.category.eq(category) : null;
    }

    private BooleanExpression cursorIdGt(Integer cursorId) {
        return cursorId != null ? showDetail.id.gt(cursorId) : null;
    }

    private BooleanExpression showDetailIdEq(Integer showDetailId) {
        return showDetailId != null ? showDetail.id.eq(showDetailId) : null;
    }
}
