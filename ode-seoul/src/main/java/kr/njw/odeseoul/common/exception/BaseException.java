package kr.njw.odeseoul.common.exception;

import kr.njw.odeseoul.common.dto.BaseResponseStatus;

public class BaseException extends RuntimeException {
    private final BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }

    public BaseResponseStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.status.getMessage();
    }
}
