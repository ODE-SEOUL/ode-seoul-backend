package kr.njw.odeseoul.recruit.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApplyRecruitResponse {
    @Schema(description = "모집 참여 아이디", example = "10")
    private Long id;
}
