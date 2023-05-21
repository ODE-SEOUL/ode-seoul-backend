package kr.njw.odeseoul.recruit.repository;

import kr.njw.odeseoul.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {
}
