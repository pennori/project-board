package com.board.api.domain.member.controller;

import com.board.api.domain.member.dto.InquiryPoint;
import com.board.api.domain.member.dto.SignUp;
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
    public ResponseEntity<ApiResponse<SignUp>> doSignUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        SignUp signUp = memberService.createMember(request);

        return ResponseEntity.ok().body(
                ApiResponse.<SignUp>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(signUp)
                        .build()
        );
    }

    @GetMapping("/point")
    public ResponseEntity<ApiResponse<InquiryPoint>> inquiryPoint() {
        InquiryPoint inquiryPoint = memberPointService.getPoint();

        return ResponseEntity.ok()
                .body(ApiResponse.<InquiryPoint>builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMessage(HttpStatus.OK.name())
                        .data(inquiryPoint)
                        .build()
                );
    }

}
