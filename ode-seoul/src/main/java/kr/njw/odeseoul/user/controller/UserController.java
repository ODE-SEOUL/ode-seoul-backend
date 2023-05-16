package kr.njw.odeseoul.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.user.application.UserProvider;
import kr.njw.odeseoul.user.application.UserService;
import kr.njw.odeseoul.user.application.dto.EditProfileRequest;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;
import kr.njw.odeseoul.user.controller.dto.EditProfileRestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
            @Parameter(description = "오디서울 아이디", example = "1") @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(new BaseResponse<>(this.userProvider.findUser(id)));
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
}
