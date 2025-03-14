package com.board.api.global.handler;

import com.board.api.domain.member.exception.MemberException;
import com.board.api.domain.post.exception.PostException;
import com.board.api.global.dto.ErrorMessage;
import com.board.api.global.dto.response.ApiResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(HashMap::new, (map, error) -> map.put(error.getField(), error.getDefaultMessage()), HashMap::putAll);

        return ResponseEntity.badRequest().body(
                ApiResponse.<Map<String, String>>builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .data(errors)
                        .build()
        );
    }

    @ExceptionHandler(value = {FeignException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> feignExceptionHandler(final FeignException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {MemberException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> memberExceptionHandler(final MemberException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {PostException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> postExceptionHandler(final PostException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> runtimeExceptionHandler(final RuntimeException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> illegalArgumentExceptionHandler(final IllegalArgumentException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> illegalStateExceptionHandler(final IllegalStateException ex) {
        return ResponseEntity.internalServerError().body(
                ApiResponse.<ErrorMessage>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(new ErrorMessage(ex.getMessage()))
                        .build()
        );
    }
}
