package kr.njw.odeseoul.notice.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindNoticeResponse {
    @Schema(description = "공지사항 아이디", example = "7")
    private Long id;
    @Schema(description = "글쓴이", example = "관리자")
    private String author;
    @Schema(description = "제목", example = "첫 공지")
    private String title;
    @Schema(description = "내용", example = "여러분들을 환영합니다.")
    private String content;
    @Schema(description = "공지 작성일시", type = "string", example = "2022-10-17T13:01:53")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
