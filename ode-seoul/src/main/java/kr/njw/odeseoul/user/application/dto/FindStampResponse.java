package kr.njw.odeseoul.user.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindStampResponse {
    @Schema(description = "코스 아이디", example = "4")
    private Long courseId;

    @Schema(description = "스탬프 획득 일시", type = "string", example = "2023-05-21T14:08:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
