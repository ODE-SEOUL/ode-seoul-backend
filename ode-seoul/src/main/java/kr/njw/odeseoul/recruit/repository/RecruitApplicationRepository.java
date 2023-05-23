package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.Recruit;
import kr.njw.odeseoul.recruit.entity.RecruitApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitApplicationRepository extends JpaRepository<RecruitApplication, Long> {
    @EntityGraph(attributePaths = {"recruit", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitApplication> findAllByRecruitIdAndDeletedAtIsNullOrderByIdAsc(Long recruitId);

    @EntityGraph(attributePaths = {"recruit", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitApplication> findAllByMemberIdAndDeletedAtIsNullAndRecruitProgressStatusAndRecruitDeletedAtIsNull(Long memberId, Recruit.RecruitProgressStatus recruitProgressStatus);

    Optional<RecruitApplication> findByRecruitIdAndMemberIdAndDeletedAtIsNull(Long recruitId, Long memberId);
}
