package kr.njw.odeseoul.recruit.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.njw.odeseoul.common.dto.PageResponse;
import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRecruitsResponse extends PageResponse {
    private List<SearchRecruitResponse> recruits;

    @Data
    public static class SearchRecruitResponse {
        @Schema(description = "모집 아이디", example = "2")
        private Long id;

        private FindRecruitResponse.FindRecruitResponseUser host;

        @Schema(description = "코스 아이디", example = "4")
        private Long courseId;

        @Schema(description = "모집 카테고리", example = "COM_PHOTO")
        private Recruit.RecruitCategory category;

        @Schema(description = "제목", example = "모집")
        private String title;

        @Schema(description = "내용", example = "여러분을 모집합니다", nullable = true)
        private String content;

        @Schema(description = "대표 이미지", example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/img_20230521125825735", nullable = true)
        private String image;

        @Schema(description = "모집 참가인원수 (모임장 본인 포함)", example = "1")
        private int currentPeople;

        @Schema(description = "모집 최대인원수 (모임장 본인 포함, 0: 제한없음)", example = "5")
        private int maxPeople;

        @Schema(description = "약속 일정", type = "string", example = "2023-05-21T14:08:45")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime scheduledAt;

        @Schema(description = """
                모집 상태

                OPEN: 모집 중\040\040
                CLOSED: 모집 마감 (현재 날짜나 참가인원수에 관계 없이 모임장이 직접 모집 마감 처리를 해야 마감됨)\040\040
                DONE: 활동 완료 (모임장이 직접 활동 완료 처리를 해야 완료됨)""", example = "OPEN")
        private Recruit.RecruitProgressStatus progressStatus;

        @Schema(description = "모집 등록일시", type = "string", example = "2023-05-21T09:33:02")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
    }
}
