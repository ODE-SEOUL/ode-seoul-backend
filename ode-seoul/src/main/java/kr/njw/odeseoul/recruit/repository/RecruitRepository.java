package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {
    @EntityGraph(attributePaths = {"host"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Recruit> findByIdAndDeletedAtIsNull(Long id);
}
