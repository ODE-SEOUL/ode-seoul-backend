package kr.njw.odeseoul.recruit.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.repository.dto.SearchRecruitsRepoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kr.njw.odeseoul.recruit.entity.QRecruit.recruit;
import static kr.njw.odeseoul.recruit.entity.QRecruitApplication.recruitApplication;
import static kr.njw.odeseoul.user.entity.QUser.user;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Recruit> search(SearchRecruitsRepoRequest request) {
        List<Recruit> recruits = this.jpaQueryFactory
                .selectFrom(recruit)
                .join(recruit.host, user).fetchJoin()
                .where(this.createSearchCondition(request))
                .orderBy(new OrderSpecifier<>(Order.DESC, recruit.id))
                .offset(request.getPageable().getOffset())
                .limit(request.getPageable().getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(recruit)
                .where(this.createSearchCondition(request))
                .fetchOne();

        return new PageImpl<>(recruits, request.getPageable(), Objects.requireNonNull(totalSize));
    }

    private BooleanBuilder createSearchCondition(SearchRecruitsRepoRequest request) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (request.getCategory() != null) {
            booleanBuilder.and(recruit.category.eq(request.getCategory()));
        }

        if (request.getCourseId() != null) {
            booleanBuilder.and(recruit.course.id.eq(request.getCourseId()));
        }

        if (request.getHostUserId() != null) {
            booleanBuilder.and(recruit.host.id.eq(request.getHostUserId()));
        }

        if (request.getMemberUserId() != null) {
            booleanBuilder.and(recruit.id.in(JPAExpressions
                    .select(recruitApplication.recruit.id)
                    .from(recruitApplication)
                    .where(recruitApplication.member.id.eq(request.getMemberUserId()),
                            recruitApplication.deletedAt.isNull())
            ));
        }

        if (request.getProgressStatusOneOf() != null) {
            booleanBuilder.and(recruit.progressStatus.in(request.getProgressStatusOneOf()));
        }

        if (request.getTitleContains() != null) {
            booleanBuilder.and(recruit.title.containsIgnoreCase(request.getTitleContains()));
        }

        booleanBuilder.and(recruit.deletedAt.isNull());

        return booleanBuilder;
    }
}
