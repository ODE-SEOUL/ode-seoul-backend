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
import kr.njw.odeseoul.recruit.controller.dto.ChangeRecruitProgressRestRequest;
import kr.njw.odeseoul.recruit.controller.dto.CreateRecruitRestRequest;
import kr.njw.odeseoul.recruit.controller.dto.WriteCommentRestRequest;
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

            @Parameter(description = "모집 상태 (1: 모집 중 [OPEN], 0: 모집 중 아님 [CLOSED 혹은 DONE]): *다음과 일치*", example = "1")
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

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 상태 변경", description = """
            모집의 상태에는 OPEN(모집 중), CLOSED(모집 마감), DONE(활동 완료)가 존재함\040\040
            최초 상태는 OPEN으로 시작함

            모집 상태가 OPEN이면 참여 요청을 받을 수 있음 (최대인원수가 다 차지 않았다면)\040\040
            모집 상태를 CLOSED로 변경하면 새로운 참여자를 받을 수 없음\040\040
            모집 상태를 DONE으로 변경하면 모임장과 참여자들에게 해당 생태문화길의 스탬프가 찍힘

            모집 상태는 현재 날짜나 참가인원수에 관계 없이 모임장이 직접 상태 변경을 했을 때만 변경됨

            제약사항: 본인이 모임장이어야 상태를 변경할 수 있음\040\040
            현재 상태가 OPEN이면 반드시 CLOSED 혹은 DONE 상태로만 변경할 수 있음\040\040
            현재 상태가 CLOSED이면 반드시 DONE 상태로만 변경할 수 있음\040\040
            현재 상태가 DONE이면 더 이상 상태를 변경할 수 없음""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    모집을 찾을 수 없습니다. (code: 14500)

                    모집 마감 혹은 활동 완료 상태로만 변경할 수 있습니다. (code: 14502)

                    활동 완료 상태로만 변경할 수 있습니다. (code: 14503)

                    이미 활동 완료된 모집은 상태를 변경할 수 없습니다. (code: 14504)""", content = @Content()),
            @ApiResponse(responseCode = "403", description = """
                    모임장만 변경할 수 있습니다. (code: 14501)""", content = @Content())
    })
    @PatchMapping("/{id}/progress")
    public ResponseEntity<BaseResponse<Boolean>> changeRecruitProgress(
            Principal principal,
            @Parameter(description = "모집 아이디", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody ChangeRecruitProgressRestRequest restRequest) {

        ChangeRecruitProgressRequest request = new ChangeRecruitProgressRequest();
        request.setRecruitId(id);
        request.setHostUserId(Long.valueOf(principal.getName()));
        request.setProgressStatus(restRequest.getProgressStatus());

        this.recruitService.changeRecruitProgress(request);
        return ResponseEntity.ok(new BaseResponse<>(true));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 참여", description = """
            제약사항: 본인이 모임장이거나 혹은 이미 참여 중인 모집에 또 참여 요청을 할 수 없음.
            모집 상태가 OPEN이 아니거나 모집 최대인원수가 모두 찬 경우는 참여 요청을 할 수 없음

            참여하면 모집에 남은 자리가 하나 줄어들게 됨""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    모집을 찾을 수 없습니다. (code: 14200)

                    잘못된 유저입니다. (code: 14201)

                    본인이 모임장인 모집입니다. (code: 14202)

                    이미 참여 중인 모집입니다. (code: 14203)

                    마감된 모집입니다. (code: 14204)

                    정원이 모두 찬 모집입니다. (code: 14205)""", content = @Content())
    })
    @PostMapping("/{id}/applications")
    public ResponseEntity<BaseResponse<ApplyRecruitResponse>> applyRecruit(
            Principal principal,
            @Parameter(description = "모집 아이디", example = "1") @PathVariable("id") Long id) {
        ApplyRecruitRequest request = new ApplyRecruitRequest();
        request.setRecruitId(id);
        request.setMemberUserId(Long.valueOf(principal.getName()));
        return ResponseEntity.ok(new BaseResponse<>(this.recruitService.applyRecruit(request)));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 참여 취소", description = """
            본인이 모집에 참여한 것을 취소할 수 있음 (자기가 모임장인 경우는 취소할 수 없음)

            참여를 취소하면 모집에 빈 자리가 하나 생기게 됨

            해당 참여 내역이 없는 경우에도 별도 오류 없이 성공이 응답됨 (이미 없는 것이므로 성공 처리)""")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{id}/applications/me")
    public ResponseEntity<BaseResponse<Boolean>> cancelRecruitApplication(
            Principal principal,
            @Parameter(description = "모집 아이디", example = "1") @PathVariable("id") Long id) {
        CancelRecruitApplicationRequest request = new CancelRecruitApplicationRequest();
        request.setRecruitId(id);
        request.setMemberUserId(Long.valueOf(principal.getName()));

        this.recruitService.cancelRecruitApplication(request);
        return ResponseEntity.ok(new BaseResponse<>(true));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    모집을 찾을 수 없습니다. (code: 14300)

                    잘못된 유저입니다. (code: 14301)""", content = @Content())
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<BaseResponse<WriteCommentResponse>> writeComment(
            Principal principal,
            @Parameter(description = "모집 아이디", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody WriteCommentRestRequest restRequest) {

        WriteCommentRequest request = new WriteCommentRequest();
        request.setRecruitId(id);
        request.setUserId(Long.valueOf(principal.getName()));
        request.setContent(restRequest.getContent());

        return ResponseEntity.ok(new BaseResponse<>(this.recruitService.writeComment(request)));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "모집 댓글 삭제", description = """
            제약사항: 본인의 댓글만 삭제할 수 있음

            해당 댓글이 존재하지 않는 경우에도 별도 오류 없이 성공이 응답됨 (이미 없는 것이므로 성공 처리)""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", description = """
                    본인의 댓글이 아닙니다. (code: 14400)""", content = @Content())
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Boolean>> deleteComment(
            Principal principal,
            @Parameter(description = "모집 댓글 아이디", example = "75") @PathVariable("commentId") Long commentId) {

        DeleteCommentRequest request = new DeleteCommentRequest();
        request.setUserId(Long.valueOf(principal.getName()));
        request.setRecruitCommentId(commentId);

        this.recruitService.deleteComment(request);
        return ResponseEntity.ok(new BaseResponse<>(true));
    }
}
