package kr.njw.odeseoul.recruit.application.dto;

import lombok.Data;

@Data
public class WriteCommentRequest {
    private Long recruitId;
    private Long userId;
    private String content;
}
