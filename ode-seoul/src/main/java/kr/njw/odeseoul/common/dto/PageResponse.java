package kr.njw.odeseoul.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageResponse {
    @Schema(description = "현재 페이지 번호", example = "2")
    int page;
    @Schema(description = "현재 페이지의 아이템 개수", example = "18")
    int pageSize;
    @Schema(description = "페이징 사이즈", example = "20")
    int pagingSize;
    @Schema(description = "총 페이지 개수", example = "3")
    int totalPage;
    @Schema(description = "총 아이템 개수", example = "38")
    long totalSize;
}
