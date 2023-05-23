package kr.njw.odeseoul.user.controller;

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
import kr.njw.odeseoul.user.application.UserProvider;
import kr.njw.odeseoul.user.application.UserService;
import kr.njw.odeseoul.user.application.dto.*;
import kr.njw.odeseoul.user.controller.dto.EditPickedCoursesRestRequest;
import kr.njw.odeseoul.user.controller.dto.EditProfileRestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserProvider userProvider;
    private final UserService userService;

    @Operation(summary = "유저 정보", description = "비로그인 상태에서도 볼 수 있는 공개된 유저 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다. (code: 11000)", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<FindUserResponse>> findUser(
            @Parameter(description = "오디서울 유저 아이디", example = "1") @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(new BaseResponse<>(this.userProvider.findUser(id)));
    }

    @Operation(summary = "유저의 스탬프 목록", description = """
            자신이 참여 중인 모집의 상태가 DONE(활동 완료)인 경우 해당 생태문화길의 스탬프를 가지게 됨

            스탬프는 생태문화길 별로 하나씩만 가질 수 있음""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다. (code: 11400)", content = @Content())
    })
    @GetMapping("/{id}/stamps")
    public ResponseEntity<BaseResponse<List<FindStampResponse>>> findStamps(
            @Parameter(description = "오디서울 유저 아이디", example = "1") @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(new BaseResponse<>(this.userProvider.findStamps(id)));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "내 프로필 수정", description = """
            내 프로필에서 바꿀 필드만 요청에 포함하면 됨

            수정하지 않을 필드는 요청에 포함하지 않거나 null로 보내면 됨""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 유저입니다. (code: 11100)

                    지역 정보가 올바르지 않습니다. (code: 11101)""", content = @Content())
    })
    @PatchMapping("/me/profile")
    public ResponseEntity<BaseResponse<Boolean>> editProfile(Principal principal, @Valid @RequestBody EditProfileRestRequest restRequest) {
        EditProfileRequest request = new EditProfileRequest();
        request.setId(Long.valueOf(principal.getName()));
        request.setNickname(restRequest.getNickname());
        request.setProfileImage(restRequest.getProfileImage());
        request.setLocationCode(restRequest.getLocationCode());

        this.userService.editProfile(request);

        return ResponseEntity.ok(new BaseResponse<>(true));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "내가 찜한 코스 목록", description = """
            가장 최근에 찜한 순서대로 정렬되어 반환

            코스의 상세한 정보는 코스 목록 API 활용 요망""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 유저입니다. (code: 11300)""", content = @Content())
    })
    @GetMapping("/me/picked-courses")
    public ResponseEntity<BaseResponse<List<FindPickedCourseResponse>>> findPickedCourses(Principal principal) {
        return ResponseEntity.ok(new BaseResponse<>(this.userProvider.findPickedCourses(Long.valueOf(principal.getName()))));
    }

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "코스 찜 등록/해제")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 유저입니다. (code: 11200)

                    잘못된 코스입니다. (code: 11201)""", content = @Content())
    })
    @PatchMapping("/me/picked-courses")
    public ResponseEntity<BaseResponse<Boolean>> editPickedCourses(Principal principal,
                                                                   @Valid @RequestBody EditPickedCoursesRestRequest restRequest) {
        EditPickedCoursesRequest request = new EditPickedCoursesRequest();

        switch (restRequest.getType()) {
            case "add" -> request.setEditType(EditPickedCoursesRequest.EditType.ADD);
            case "remove" -> request.setEditType(EditPickedCoursesRequest.EditType.REMOVE);
            default -> throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }

        request.setUserId(Long.valueOf(principal.getName()));
        request.setCourseId(restRequest.getCourseId());

        this.userService.editPickedCourses(request);

        return ResponseEntity.ok(new BaseResponse<>(true));
    }
}
