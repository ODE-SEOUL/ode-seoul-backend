package kr.njw.odeseoul.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.user.application.UserProvider;
import kr.njw.odeseoul.user.application.dto.FindUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserProvider userProvider;

    @Operation(summary = "회원 정보", description = "비로그인 상태에서도 볼 수 있는 공개된 회원 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다. (code: 11000)", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<FindUserResponse>> findUser(
            @Parameter(description = "오디서울 아이디", example = "1") @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(new BaseResponse<>(this.userProvider.findUser(id)));
    }
}
