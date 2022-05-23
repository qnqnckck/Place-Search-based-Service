package com.kelvin.psbs.common.aop;

import com.kelvin.psbs.common.exception.PbssException;
import com.kelvin.psbs.common.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleServerError(Exception e) {
        log.error("Exception message={}", e.getMessage(), e);
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(PbssException.class)
    public ResponseEntity<CommonResponse<Object>> handlePbssError(Exception e) {
        // 5XX의 경우 Server 에러는 error 로 출력
        log.error("Exception message={}", e.getMessage(), e);
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<CommonResponse<Object>> handleBadRequest(Exception e) {
        //  4XX의 경우 Client 에러이므로 info로 출력
        log.info("Exception message={}", e.getMessage(), e);
        return handleError(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<CommonResponse<Object>> handleError(HttpStatus responseStatus, Throwable e) {
        return handleError(responseStatus, e, e.getMessage());
    }

    private ResponseEntity<CommonResponse<Object>> handleError(HttpStatus responseStatus, Throwable e, String message) {
        return ResponseEntity.status(responseStatus).body(CommonResponse.of(responseStatus, message));
    }
}

