package kr.njw.odeseoul.user.repository;

import kr.njw.odeseoul.user.entity.UserPickedCourse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPickedCourseRepository extends JpaRepository<UserPickedCourse, Long> {
    Optional<UserPickedCourse> findFirstByUserIdAndCourseIdAndDeletedAtIsNull(Long userId, Long courseId);

    @EntityGraph(attributePaths = {"course"}, type = EntityGraph.EntityGraphType.LOAD)
    List<UserPickedCourse> findAllByUserIdAndDeletedAtIsNullOrderByIdDesc(Long userId);
}
