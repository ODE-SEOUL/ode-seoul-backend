package kr.njw.odeseoul.user.application.dto;

import lombok.Data;

@Data
public class EditPickedCoursesRequest {
    private EditType editType;
    private Long userId;
    private Long courseId;

    public enum EditType {
        ADD,
        REMOVE
    }
}
