package kr.njw.odeseoul.course.repository;

import kr.njw.odeseoul.course.entity.CourseReview;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    @EntityGraph(attributePaths = {"course", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<CourseReview> findAllByCourseIdAndDeletedAtIsNullOrderByIdDesc(Long courseId);

    @EntityGraph(attributePaths = {"course", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<CourseReview> findAllByUserIdAndDeletedAtIsNullOrderByIdDesc(Long userId);

    @EntityGraph(attributePaths = {"course", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<CourseReview> findByCourseIdAndUserIdAndDeletedAtIsNull(Long courseId, Long userId);
}
