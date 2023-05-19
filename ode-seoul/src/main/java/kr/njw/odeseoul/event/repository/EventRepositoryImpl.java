package kr.njw.odeseoul.event.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.njw.odeseoul.event.entity.Event;
import kr.njw.odeseoul.event.repository.dto.SearchEventsRepoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kr.njw.odeseoul.event.entity.QEvent.event;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Event> search(SearchEventsRepoRequest request) {
        List<Event> events = this.jpaQueryFactory
                .selectFrom(event)
                .where(this.createSearchCondition(request))
                .orderBy(new OrderSpecifier<?>[]{
                        new OrderSpecifier<>(Order.DESC, event.registerDate),
                        new OrderSpecifier<>(Order.DESC, event.id)
                })
                .offset(request.getPageable().getOffset())
                .limit(request.getPageable().getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(event)
                .where(this.createSearchCondition(request))
                .fetchOne();

        return new PageImpl<>(events, request.getPageable(), Objects.requireNonNull(totalSize));
    }

    private BooleanBuilder createSearchCondition(SearchEventsRepoRequest request) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (request.getCodenamesOneOf() != null) {
            booleanBuilder.and(event.codename.in(request.getCodenamesOneOf()));
        }

        if (request.getGunamesOneOf() != null) {
            booleanBuilder.and(event.guname.in(request.getGunamesOneOf()));
        }

        if (request.getTitleContains() != null) {
            booleanBuilder.and(event.title.containsIgnoreCase(request.getTitleContains()));
        }

        return booleanBuilder;
    }
}
