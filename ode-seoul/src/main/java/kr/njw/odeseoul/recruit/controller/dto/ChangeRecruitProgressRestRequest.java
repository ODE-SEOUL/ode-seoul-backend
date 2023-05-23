package kr.njw.odeseoul.recruit.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;

@Data
public class ChangeRecruitProgressRestRequest {
    @Schema(description = """
            모집 상태

            OPEN: 모집 중\040\040
            CLOSED: 모집 마감 (현재 날짜나 참가인원수에 관계 없이 모임장이 직접 모집 마감 처리를 해야 마감됨)\040\040
            DONE: 활동 완료 (모임장이 직접 활동 완료 처리를 해야 완료됨)""", example = "CLOSED")
    @NotNull(message = "must not be null")
    private Recruit.RecruitProgressStatus progressStatus;
}
