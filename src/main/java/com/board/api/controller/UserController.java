package com.board.api.controller;

import com.board.api.dto.SignUpRequest;
import com.board.api.dto.SignUpResponse;
import com.board.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> doSignUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        SignUpResponse response = new SignUpResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST.name());
        long userSeq = memberService.saveMember(request);
        if(0L != userSeq) {
            response = new SignUpResponse(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name());
        }

        return ResponseEntity.ok().body(response);
    }

}
