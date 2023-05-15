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

    // course (12xxx)
    WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_COURSE(HttpStatus.BAD_REQUEST, 12000, "코스가 존재하지 않습니다."),
    WRITE_COURSE_REVIEW_ERROR_NOT_FOUND_USER(HttpStatus.BAD_REQUEST, 12001, "잘못된 유저입니다."),
    WRITE_COURSE_REVIEW_ERROR_ALREADY_WRITTEN(HttpStatus.BAD_REQUEST, 12002, "이미 리뷰를 작성했습니다."),

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
