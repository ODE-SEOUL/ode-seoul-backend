package kr.njw.odeseoul.course.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.course.application.CourseProvider;
import kr.njw.odeseoul.course.application.dto.FindCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseProvider courseProvider;

    @Operation(summary = "코스 목록", description = """
            모든 코스 목록을 불러오는 API라서 여러 페이지에서 활용할 수 있음

            요청 처리 시간으로 인해 프론트에서는 최초 1회만 API 호출 후 변수에 저장해서 여러 페이지에서 활용하기를 요망 (추가적인 필터링이나 정렬 작업 등 진행 요망)""")
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<FindCourseResponse>>> findCourses(
            @Parameter(description = "(optional) 검색 위치 위도 (distanceFromSearchLocation을 받고 싶을 때 포함)", example = "37.65251105")
            @RequestParam(value = "latitude", required = false) Double latitude,
            @Parameter(description = "(optional) 검색 위치 경도 (distanceFromSearchLocation을 받고 싶을 때 포함)", example = "127.0750347")
            @RequestParam(value = "longitude", required = false) Double longitude
    ) {
        return ResponseEntity.ok(new BaseResponse<>(this.courseProvider.findCourses(
                (latitude != null && longitude != null) ? Pair.of(latitude, longitude) : null)));
    }
}
