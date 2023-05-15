package kr.njw.odeseoul.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.image.application.ImageService;
import kr.njw.odeseoul.image.application.dto.UploadImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @SecurityRequirement(name = "accessToken")
    @Operation(summary = "이미지 업로드", description = """
            이미지 업로드가 필요한 모든 상황에서 사용하는 API (개발 과정 or 실서비스 내에서 자유롭게 사용 가능)

            multipart/form-data 형식으로 최대 10MB 업로드 가능

            multipart/form-data 요청시 파일 필드는 `file`로 지정""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "파일 용량이 초과되었습니다. (code: 99000)", content = @Content())
    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<UploadImageResponse>> uploadImage(
            @Parameter(description = "이미지 파일", required = true) @RequestPart("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(new BaseResponse<>(this.imageService.uploadImage(file)));
    }
}
