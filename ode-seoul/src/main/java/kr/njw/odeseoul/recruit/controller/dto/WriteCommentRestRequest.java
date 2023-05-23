package kr.njw.odeseoul.recruit.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WriteCommentRestRequest {
    @Schema(description = "내용", example = "안녕하세요")
    @NotEmpty(message = "must not be empty")
    private String content;
}
