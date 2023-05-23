package kr.njw.odeseoul.recruit.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplyRecruitResponse {
    @Schema(description = "모집 참여 아이디", example = "10")
    private Long id;

    @Schema(description = "신청일시", type = "string", example = "2023-05-21T09:33:02")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
