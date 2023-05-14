package kr.njw.odeseoul.location.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindSeoulGugunLocationResponse {
    @Schema(description = "행정동코드", example = "1135000000")
    private String code;
    @Schema(example = "노원구")
    private String name;
    @Schema(description = "자치구 중심좌표 위도", example = "37.65251105")
    private Double latitude;
    @Schema(description = "자치구 중심좌표 경도", example = "127.0750347")
    private Double longitude;
}
