package kr.njw.odeseoul.course.repository;

import kr.njw.odeseoul.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByDeletedAtIsNullOrderById();
}
