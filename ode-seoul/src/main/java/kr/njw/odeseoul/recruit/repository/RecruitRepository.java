package kr.njw.odeseoul.recruit.repository;

import jakarta.persistence.LockModeType;
import kr.njw.odeseoul.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {
    @EntityGraph(attributePaths = {"host"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Recruit> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = {"host"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Recruit> findAllByHostIdAndProgressStatusAndDeletedAtIsNull(Long hostId, Recruit.RecruitProgressStatus recruitProgressStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"host"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Recruit> findForUpdateByIdAndDeletedAtIsNull(Long id);
}
