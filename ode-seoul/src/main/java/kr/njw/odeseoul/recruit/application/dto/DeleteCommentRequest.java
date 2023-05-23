package kr.njw.odeseoul.recruit.application.dto;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    private Long userId;
    private Long recruitCommentId;
}
