package com.board.api.domain.member.controller;

import com.board.api.domain.member.dto.CurrentPointDto;
import com.board.api.domain.member.dto.SignUpDto;
import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.service.MemberPointService;
import com.board.api.domain.member.service.MemberService;
import com.board.api.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberPointService memberPointService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpDto>> doSignUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        SignUpDto signUpDto = memberService.createMember(request);

        return ResponseEntity.ok().body(
                ApiResponse.<SignUpDto>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(signUpDto)
                        .build()
        );
    }

    @GetMapping("/point")
    public ResponseEntity<ApiResponse<CurrentPointDto>> getCurrentPoint() {
        CurrentPointDto currentPointDto = memberPointService.getPoint();

        return ResponseEntity.ok()
                .body(ApiResponse.<CurrentPointDto>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(currentPointDto)
                        .build()
                );
    }

}
