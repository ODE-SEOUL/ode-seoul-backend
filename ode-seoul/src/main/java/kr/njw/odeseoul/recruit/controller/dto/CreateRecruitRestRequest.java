package kr.njw.odeseoul.recruit.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.njw.odeseoul.recruit.entity.Recruit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRecruitRestRequest {
    @Schema(description = "코스 아이디", example = "4")
    @NotNull(message = "must not be null")
    private Long courseId;

    @Schema(description = "모집 카테고리", example = "COM_PHOTO")
    @NotNull(message = "must not be null")
    private Recruit.RecruitCategory category;

    @Schema(description = "제목", example = "모집")
    @NotBlank(message = "must not be blank")
    private String title;

    @Schema(description = "내용", example = "여러분을 모집합니다")
    private String content;

    @Schema(description = "대표 이미지", example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/img_20230521125825735")
    private String image;

    @Schema(description = "모집 최대인원수 (모임장 본인 포함, 0: 제한없음)", example = "5")
    @NotNull(message = "must not be null")
    @Min(value = 0, message = "must be greater than or equal to 0")
    private int maxPeople;

    @Schema(description = "약속 일정 (미래 날짜가 아니면 Validation 오류 발생)", type = "string", example = "2024-05-21T14:08:45")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "must not be null")
    @Future(message = "must be a future date")
    private LocalDateTime scheduledAt;
}
