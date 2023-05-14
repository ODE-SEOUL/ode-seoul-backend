package kr.njw.odeseoul.image.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UploadImageResponse {
    @Schema(example = "https://ik.imagekit.io/njw1204/ode-seoul/20230514232333_7wxeJV0z9")
    private String url;
}
