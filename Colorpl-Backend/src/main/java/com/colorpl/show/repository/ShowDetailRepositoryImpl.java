package com.colorpl.show.repository;

import static com.colorpl.show.domain.QShowDetail.showDetail;
import static com.colorpl.show.domain.QShowSchedule.showSchedule;
import static com.colorpl.theater.domain.QHall.hall;
import static com.colorpl.theater.domain.QTheater.theater;

import com.colorpl.show.domain.Area;
import com.colorpl.show.domain.Category;
import com.colorpl.show.domain.ShowDetail;
import com.colorpl.show.dto.GetShowDetailsRequest;
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
    public List<ShowDetail> getShowDetails(GetShowDetailsRequest request) {
        return queryFactory
            .select(showDetail).distinct()
            .from(showDetail)
            .join(showDetail.showSchedules, showSchedule).fetchJoin()
            .where(
                dateEq(request.getDate()),
                areaEq(request.getArea()),
                nameContains(request.getKeyword()),
                categoryEq(request.getCategory())
            )
            .fetch();
    }

    @Override
    public ShowDetail getShowDetail(Integer showDetailId) {
        return queryFactory
            .select(showDetail).distinct()
            .from(showDetail)
            .join(showDetail.showSchedules, showSchedule).fetchJoin()
            .where(idEq(showDetailId))
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
                idEq(condition.getShowDetailId()),
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

    private BooleanExpression areaEq(Area area) {
        return area != null ? showDetail.area.eq(area) : null;
    }

    private BooleanExpression nameContains(String keyword) {
        return keyword != null ? showDetail.name.contains(keyword) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? showDetail.category.eq(category) : null;
    }

    private BooleanExpression idEq(Integer id) {
        return id != null ? showDetail.id.eq(id) : null;
    }
}
