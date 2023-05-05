package kr.njw.odeseoul.common.controller;

import io.swagger.v3.oas.annotations.Hidden;
import kr.njw.odeseoul.common.dto.BaseResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Hidden
@RestController
public class BaseErrorController implements ErrorController {
    @RequestMapping("/error")
    public BaseResponse<?> error(HttpServletResponse response) {
        return new BaseResponse<>(response.getStatus(), "[BaseError] " + HttpStatus.valueOf(response.getStatus()).getReasonPhrase(), null);
    }
}
