package kr.njw.odeseoul.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import kr.njw.odeseoul.common.dto.BaseResponse;
import kr.njw.odeseoul.common.dto.BaseResponseStatus;
import kr.njw.odeseoul.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMethodException(HttpRequestMethodNotSupportedException e) {
        BaseResponse<?> response = new BaseResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " +
                        Objects.requireNonNullElse(error.getDefaultMessage(), "")).trim()).toList();
        BaseResponse<?> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), StringUtils.join(errors, ", "), null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        BaseResponse<?> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<?>> handleBindException(BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        return ResponseEntity.status(e.getStatus().getHttpStatus()).body(new BaseResponse<>(e.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleException(Exception e, HttpServletRequest request) {
        log.error("[Unknown Exception] method: {} | uri: {} | message: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    }
}
