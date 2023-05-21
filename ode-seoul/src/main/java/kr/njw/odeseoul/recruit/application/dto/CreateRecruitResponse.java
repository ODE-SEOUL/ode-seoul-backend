package kr.njw.odeseoul.recruit.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateRecruitResponse {
    @Schema(description = "모집 아이디", example = "2")
    private Long id;
}
