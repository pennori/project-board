package com.board.api.domain.member.controller;

import com.board.api.domain.member.dto.CurrentPointDto;
import com.board.api.domain.member.dto.SignUpDto;
import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.service.MemberPointService;
import com.board.api.domain.member.service.MemberService;
import com.board.api.global.dto.response.ApiResponse;
import com.board.api.global.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResponse<SignUpDto>> signUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        log.info("회원 가입 요청: {}", request);
        SignUpDto signUpDto = memberService.createMember(request);
        log.info("회원 가입 성공: memberId={}", signUpDto.getMemberId());
        return ResponseUtil.buildResponseWithData(signUpDto);
    }

    @GetMapping("/point")
    public ResponseEntity<ApiResponse<CurrentPointDto>> getMemberPoint() {
        log.info("회원 포인트 조회 요청");
        CurrentPointDto currentPointDto = memberPointService.getPoint();
        log.info("회원 포인트 조회 결과: {}", currentPointDto.getPoint());
        return ResponseUtil.buildResponseWithData(currentPointDto);
    }

}