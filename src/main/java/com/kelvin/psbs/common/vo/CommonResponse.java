package com.kelvin.psbs.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String message;
    private T body;

    public static <T> CommonResponse<T> of(T body) {
        return new CommonResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                body
        );
    }

    public static <T> CommonResponse<T> of(HttpStatus httpStatus, T body) {
        return new CommonResponse<>(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                body
        );
    }
}