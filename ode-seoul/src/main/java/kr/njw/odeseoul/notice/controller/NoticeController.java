package kr.njw.odeseoul.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.notice.application.NoticeProvider;
import kr.njw.odeseoul.notice.application.dto.FindNoticesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeProvider noticeProvider;

    @Operation(summary = "공지사항 목록", description = """
            최신순으로 정렬, 페이징 사용""")
    @GetMapping("")
    public ResponseEntity<BaseResponse<FindNoticesResponse>> findNotices(
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
        return ResponseEntity.ok(new BaseResponse<>(this.noticeProvider.findNotices(pageNumber, pagingSize)));
    }
}
