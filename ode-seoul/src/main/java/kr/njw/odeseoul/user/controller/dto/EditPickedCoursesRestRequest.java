package kr.njw.odeseoul.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EditPickedCoursesRestRequest {
    @Schema(description = "요청 타입 (add: 찜 등록, remove: 찜 해제)", example = "add")
    private String type;
    @Schema(description = "코스 아이디", example = "4")
    private Long courseId;
}
