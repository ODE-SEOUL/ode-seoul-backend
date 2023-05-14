package kr.njw.odeseoul.course.application;

import kr.njw.odeseoul.course.application.dto.FindCourseResponse;
import org.springframework.data.util.Pair;

import java.util.List;

public interface CourseProvider {
    List<FindCourseResponse> findCourses(Pair<Double, Double> searchLatLng);
}
