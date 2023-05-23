package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.RecruitComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {
    @EntityGraph(attributePaths = {"recruit", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitComment> findAllByRecruitIdAndDeletedAtIsNullOrderByIdDesc(Long recruitId);

    @EntityGraph(attributePaths = {"recruit", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<RecruitComment> findByIdAndDeletedAtIsNull(Long id);
}
