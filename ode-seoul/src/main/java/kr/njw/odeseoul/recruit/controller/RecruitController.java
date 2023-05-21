package kr.njw.odeseoul.recruit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.recruit.application.RecruitProvider;
import kr.njw.odeseoul.recruit.application.RecruitService;
import kr.njw.odeseoul.recruit.application.dto.*;
import kr.njw.odeseoul.recruit.controller.dto.CreateRecruitRestRequest;
import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruits")
public class RecruitController {
    private final RecruitProvider recruitProvider;
    private final RecruitService recruitService;

    @Operation(summary = "모집 목록", description = """
            요청에 포함한 조건을 모두 만족하는 모집 목록 반환

            원하는 조건들만 취사선택하여 조합해서 검색할 수 있음

            아무 조건을 주지 않을 수도 있음

            (참고사항: 나의 모집글과 약속 목록 구현시 유저 아이디 조건을 활용해 구현할 수 있음)""")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<SearchRecruitsResponse>> searchRecruits(
            @Parameter(description = "카테고리: *다음과 일치*", example = "COM_PHOTO")
            @RequestParam(value = "category", required = false)
            Recruit.RecruitCategory category,

            @Parameter(description = "코스(코스 아이디): *다음과 일치*", example = "1")
            @RequestParam(value = "course", required = false)
            Long courseId,

            @Parameter(description = "모임장(유저 아이디): *다음과 일치*", example = "2")
            @RequestParam(value = "host", required = false)
            Long hostUserId,

            @Parameter(description = "모임장 이외 참가자(유저 아이디): *다음을 포함*", example = "38")
            @RequestParam(value = "member", required = false)
            Long memberUserId,

            @Parameter(description = "제목: *다음 텍스트를 포함*", example = "모집")
            @RequestParam(value = "title", required = false)
            String titleContains,

            @Parameter(description = "모집 상태 (1: 모집 중, 0: 모집 중 아님): *다음과 일치*", example = "1")
            @Min(value = 0, message = "must be greater than or equal to 0")
            @Max(value = 1, message = "must be less than or equal to 1")
            @RequestParam(value = "open", required = false)
            Integer open,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "1")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ) {
        SearchRecruitsRequest request = new SearchRecruitsRequest();
        request.setCategory(category);
        request.setCourseId(courseId);
        request.setHostUserId(hostUserId);
        request.setMemberUserId(memberUserId);
        request.setTitleContains(titleContains);
        request.setOpen((open != null) ? (open != 0) : null);
        request.setPageNumber(pageNumber);
        request.setPagingSize(pagingSize);

        return ResponseEntity.ok(new BaseResponse<>(this.recruitProvider.searchRecruits(request)));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    코스가 존재하지 않습니다. (code: 14000)

                    잘못된 유저입니다. (code: 14001)""", content = @Content())
    })
    @PostMapping("")
    public ResponseEntity<BaseResponse<CreateRecruitResponse>> createRecruit(Principal principal,
                                                                             @Valid @RequestBody CreateRecruitRestRequest restRequest) {
        CreateRecruitRequest request = new CreateRecruitRequest();
        request.setHostUserId(Long.valueOf(principal.getName()));
        request.setCourseId(restRequest.getCourseId());
        request.setCategory(restRequest.getCategory());
        request.setTitle(restRequest.getTitle());
        request.setContent(restRequest.getContent());
        request.setImage(restRequest.getImage());
        request.setMaxPeople(restRequest.getMaxPeople());
        request.setScheduledAt(restRequest.getScheduledAt());

        return ResponseEntity.ok(new BaseResponse<>(this.recruitService.createRecruit(request)));
    }

    @Operation(summary = "모집 상세", description = """
            응답 중 comments 필드는 모집 페이지에 작성된 댓글 목록, applications 필드는 모집에 신청한 사람 목록임""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = """
                    모집을 찾을 수 없습니다. (code: 14100)""", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<FindRecruitResponse>> findRecruit(
            @Parameter(description = "모집 아이디", example = "1") @PathVariable("id") Long id) {
        FindRecruitRequest request = new FindRecruitRequest();
        request.setId(id);
        return ResponseEntity.ok(new BaseResponse<>(this.recruitProvider.findRecruit(request)));
    }
}
