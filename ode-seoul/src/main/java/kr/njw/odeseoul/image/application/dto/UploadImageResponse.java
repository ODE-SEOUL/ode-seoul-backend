package kr.njw.odeseoul.image.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UploadImageResponse {
    @Schema(example = "https://ik.imagekit.io/njw1204/tr:w-720,h-720,c-at_max/ode-seoul/20230515175443_pjLt7oeIx")
    private String url;
}
