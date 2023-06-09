package kr.njw.odeseoul.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.OK, 200, "요청에 성공하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "입력을 확인해주세요."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증에 실패했습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 오류가 발생했습니다."),

    // auth (10xxx)
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, 10000, "로그인에 실패했습니다."),
    SIGNUP_ERROR_BAD_USER(HttpStatus.UNAUTHORIZED, 10100, "계정 상태가 올바르지 않습니다."),
    SIGNUP_ERROR_BAD_LOCATION(HttpStatus.BAD_REQUEST, 10101, "지역 정보가 올바르지 않습니다."),
    RENEW_TOKEN_ERROR_BAD_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 10200, "리프레시 토큰이 올바르지 않습니다."),

    // user (11xxx)
    FIND_USER_ERROR_NOT_FOUND_USER(HttpStatus.NOT_FOUND, 11000, "유저를 찾을 수 없습니다."),
    EDIT_PROFILE_ERROR_BAD_USER(HttpStatus.BAD_REQUEST, 11100, "잘못된 유저입니다."),
    EDIT_PROFILE_ERROR_BAD_LOCATION(HttpStatus.BAD_REQUEST, 11101, "지역 정보가 올바르지 않습니다."),
    EDIT_PICKED_COURSES_ERROR_BAD_USER(HttpStatus.BAD_REQUEST, 11200, "잘못된 유저입니다."),
    EDIT_PICKED_COURSES_ERROR_BAD_COURSE(HttpStatus.BAD_REQUEST, 11201, "잘못된 코스입니다."),
    FIND_PICKED_COURSES_ERROR_BAD_USER(HttpStatus.BAD_REQUEST, 11300, "잘못된 유저입니다."),
    FIND_USER_STAMPS_ERROR_NOT_FOUND_USER(HttpStatus.NOT_FOUND, 11400, "유저를 찾을 수 없습니다."),

    // course (12xxx)
    WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_COURSE(HttpStatus.BAD_REQUEST, 12000, "코스가 존재하지 않습니다."),
    WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, 12001, "잘못된 유저입니다."),
    WRITE_COURSE_REVIEW_ERROR_ALREADY_WRITTEN(HttpStatus.BAD_REQUEST, 12002, "이미 리뷰를 작성했습니다."),

    // event (13xxx)
    SEARCH_EVENTS_ERROR_BAD_GUGUN(HttpStatus.BAD_REQUEST, 13000, "자치구 조건이 올바르지 않습니다."),

    // recruit (14xxx)
    CREATE_RECRUIT_ERROR_NOT_FOUND_COURSE(HttpStatus.BAD_REQUEST, 14000, "코스가 존재하지 않습니다."),
    CREATE_RECRUIT_ERROR_NOT_FOUND_HOST(HttpStatus.BAD_REQUEST, 14001, "잘못된 유저입니다."),
    FIND_RECRUIT_ERROR_NOT_FOUND(HttpStatus.NOT_FOUND, 14100, "모집을 찾을 수 없습니다."),
    APPLY_RECRUIT_ERROR_NOT_FOUND_RECRUIT(HttpStatus.BAD_REQUEST, 14200, "모집을 찾을 수 없습니다."),
    APPLY_RECRUIT_ERROR_NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, 14201, "잘못된 유저입니다."),
    APPLY_RECRUIT_ERROR_I_AM_HOST(HttpStatus.BAD_REQUEST, 14202, "본인이 모임장인 모집입니다."),
    APPLY_RECRUIT_ERROR_ALREADY_APPLIED(HttpStatus.BAD_REQUEST, 14203, "이미 참여 중인 모집입니다."),
    APPLY_RECRUIT_ERROR_NOT_OPENED_RECRUIT(HttpStatus.BAD_REQUEST, 14204, "마감된 모집입니다."),
    APPLY_RECRUIT_ERROR_RECRUIT_FULL(HttpStatus.BAD_REQUEST, 14205, "정원이 모두 찬 모집입니다."),
    WRITE_COMMENT_ERROR_NOT_FOUND_RECRUIT(HttpStatus.BAD_REQUEST, 14300, "모집을 찾을 수 없습니다."),
    WRITE_COMMENT_ERROR_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, 14301, "잘못된 유저입니다."),
    DELETE_COMMENT_ERROR_NOT_MY_COMMENT(HttpStatus.FORBIDDEN, 14400, "본인의 댓글이 아닙니다."),
    CHANGE_RECRUIT_PROGRESS_ERROR_NOT_FOUND_RECRUIT(HttpStatus.BAD_REQUEST, 14500, "모집을 찾을 수 없습니다."),
    CHANGE_RECRUIT_PROGRESS_ERROR_NOT_HOST(HttpStatus.FORBIDDEN, 14501, "모임장만 변경할 수 있습니다."),
    CHANGE_RECRUIT_PROGRESS_ERROR_ALLOW_CLOSED_OR_DONE(HttpStatus.BAD_REQUEST, 14502, "모집 마감 혹은 활동 완료 상태로만 변경할 수 있습니다."),
    CHANGE_RECRUIT_PROGRESS_ERROR_ALLOW_DONE(HttpStatus.BAD_REQUEST, 14503, "활동 완료 상태로만 변경할 수 있습니다."),
    CHANGE_RECRUIT_PROGRESS_ERROR_ALREADY_DONE(HttpStatus.BAD_REQUEST, 14504, "이미 활동 완료된 모집은 상태를 변경할 수 없습니다."),

    // etc (99xxx)
    MAX_UPLOAD_SIZE_EXCEEDED_ERROR(HttpStatus.BAD_REQUEST, 99000, "파일 용량이 초과되었습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 99999, "알 수 없는 오류입니다.");

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    BaseResponseStatus(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
