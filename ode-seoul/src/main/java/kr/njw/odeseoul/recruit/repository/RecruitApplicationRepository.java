package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.RecruitApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitApplicationRepository extends JpaRepository<RecruitApplication, Long> {
    @EntityGraph(attributePaths = {"recruit", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitApplication> findAllByRecruitIdAndDeletedAtIsNullOrderByIdAsc(Long recruitId);

    @EntityGraph(attributePaths = {"recruit", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitApplication> findAllByMemberIdAndDeletedAtIsNullOrderByIdDesc(Long memberId);
}
