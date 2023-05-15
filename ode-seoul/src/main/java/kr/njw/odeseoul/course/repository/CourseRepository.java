package kr.njw.odeseoul.course.repository;

import kr.njw.odeseoul.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByDeletedAtIsNullOrderById();

    Optional<Course> findByIdAndDeletedAtIsNull(Long id);
}
