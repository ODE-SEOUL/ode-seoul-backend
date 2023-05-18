package kr.njw.odeseoul.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.event.application.EventProvider;
import kr.njw.odeseoul.event.application.dto.SearchEventsRequest;
import kr.njw.odeseoul.event.application.dto.SearchEventsResponse;
import kr.njw.odeseoul.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventProvider eventProvider;

    @Operation(summary = "행사 목록", description = """
            요청에 포함한 조건을 모두 만족하는 행사 목록 반환

            원하는 조건들만 취사선택하여 조합해서 검색할 수 있음

            아무 조건을 주지 않을 수도 있음""")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = """
                    자치구 조건이 올바르지 않습니다. (code: 13000)""", content = @Content())
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<SearchEventsResponse>> searchEvents(
            @Parameter(description = "카테고리: *다음과 일치*", example = "SHOW")
            @RequestParam(value = "category", required = false)
            Event.EventCategory category,
            @Parameter(description = "자치구(행정동코드): *다음과 일치*", example = "1174000000")
            @RequestParam(value = "gugun", required = false)
            String gugunCode,
            @Parameter(description = "행사명: *다음 텍스트를 포함*", example = "밴드")
            @RequestParam(value = "title", required = false)
            String titleContains,
            @Parameter(description = "페이지 번호")
            @RequestParam(value = "page", defaultValue = "1")
            @Positive(message = "must be greater than 0")
            Integer pageNumber,
            @Parameter(description = "페이징 사이즈 (최대 100)")
            @RequestParam(value = "size", defaultValue = "20")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            Integer pagingSize
    ) {
        SearchEventsRequest request = new SearchEventsRequest();
        request.setCategory(category);
        request.setGugunCode(gugunCode);
        request.setTitleContains(titleContains);
        request.setPageNumber(pageNumber);
        request.setPagingSize(pagingSize);

        return ResponseEntity.ok(new BaseResponse<>(this.eventProvider.searchEvents(request)));
    }
}
