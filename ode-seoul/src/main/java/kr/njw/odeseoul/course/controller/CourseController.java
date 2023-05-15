package kr.njw.odeseoul.course.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import kr.njw.odeseoul.course.application.CourseProvider;
import kr.njw.odeseoul.course.application.CourseReviewProvider;
import kr.njw.odeseoul.course.application.CourseReviewService;
import kr.njw.odeseoul.course.application.dto.*;
import kr.njw.odeseoul.course.controller.dto.WriteCourseReviewRestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseProvider courseProvider;
    private final CourseReviewService courseReviewService;
    private final CourseReviewProvider courseReviewProvider;

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

    @Operation(summary = "리뷰 목록", description = "코스에 작성된 리뷰 목록 or 유저가 작성한 리뷰 목록 조회 가능")
    @GetMapping("/reviews")
    public ResponseEntity<BaseResponse<List<FindCourseReviewResponse>>> findCourseReviews(
            @Parameter(description = "검색 조건 (course: 코스 아이디로 검색, user: 유저 아이디로 검색)", example = "course", required = true)
            @RequestParam(value = "type") String type,
            @Parameter(description = "검색 값 (검색 조건이 course이면 코스 아이디, user이면 유저 아이디)", example = "1", required = true)
            @RequestParam(value = "value") Long value
    ) {
        FindCourseReviewsRequest request = new FindCourseReviewsRequest();

        switch (type) {
            case "course" -> {
                request.setFindType(FindCourseReviewsRequest.FindType.BY_COURSE_ID);
                request.setCourseId(value);
            }
            case "user" -> {
                request.setFindType(FindCourseReviewsRequest.FindType.BY_USER_ID);
                request.setUserId(value);
            }
            default -> throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new BaseResponse<>(this.courseReviewProvider.findCourseReviews(request)));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "리뷰 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    코스가 존재하지 않습니다. (code: 12000)

                    잘못된 유저입니다. (code: 12001)

                    이미 리뷰를 작성했습니다. (code: 12002)""", content = @Content())
    })
    @PostMapping("/reviews")
    public ResponseEntity<BaseResponse<WriteCourseReviewResponse>> writeCourseReview(Principal principal,
                                                                                     @Valid @RequestBody WriteCourseReviewRestRequest restRequest) {
        WriteCourseReviewRequest request = new WriteCourseReviewRequest();
        request.setCourseId(restRequest.getCourseId());
        request.setUserId(Long.valueOf(principal.getName()));
        request.setScore(restRequest.getScore());
        request.setContent(restRequest.getContent());
        request.setImage(restRequest.getImage());

        return ResponseEntity.ok(new BaseResponse<>(this.courseReviewService.writeCourseReview(request)));
    }
}
