package com.board.api.global.handler;

import com.board.api.global.dto.response.ErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = { FeignException.class })
    public ResponseEntity<Object> feignExceptionHandler(final FeignException ex) {
        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .resultMsg(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .build()
        );
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> runtimeExceptionHandler(final RuntimeException ex) {
        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .resultMsg(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .build()
        );
    }
}
