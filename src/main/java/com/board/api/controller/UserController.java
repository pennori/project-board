package com.board.api.controller;

import com.board.api.dto.SignUpRequest;
import com.board.api.dto.SignUpResponse;
import com.board.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponse doSignUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        SignUpResponse response = new SignUpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name());
        long userSeq = userService.saveUser(request);
        if(0L != userSeq) {
            response = new SignUpResponse(HttpStatus.OK.value(), HttpStatus.OK.name());
        }
        log.info("response : {}", response);
        return response;
    }

}
