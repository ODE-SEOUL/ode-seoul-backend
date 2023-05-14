package kr.njw.odeseoul.location.repository;

import kr.njw.odeseoul.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findByCodeAndDeletedAtIsNull(String code);

    List<Location> findAllBySeoulGugunAndDeletedAtIsNullOrderByCode(boolean seoulGugun);
}
