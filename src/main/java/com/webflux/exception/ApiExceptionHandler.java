package com.webflux.exception;

import com.webflux.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<APIResponse<String>> handleException(Exception exp) {
        APIResponse<String> res = APIResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exp.getMessage())
                .build();

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
