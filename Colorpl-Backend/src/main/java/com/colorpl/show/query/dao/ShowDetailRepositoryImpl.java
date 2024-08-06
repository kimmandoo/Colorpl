package com.colorpl.show.query.dao;

import static com.colorpl.show.domain.detail.QShowDetail.showDetail;

import com.colorpl.show.domain.detail.ShowDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowDetailRepositoryImpl implements ShowDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ShowDetail> showList() {
        return queryFactory
            .select(showDetail)
            .from(showDetail)
            .where()
            .fetch();
    }
}
