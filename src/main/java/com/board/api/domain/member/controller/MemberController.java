package com.board.api.domain.member.controller;

import com.board.api.domain.member.dto.request.SignUpRequest;
import com.board.api.domain.member.dto.response.PointResponse;
import com.board.api.domain.member.dto.response.SignUpResponse;
import com.board.api.domain.member.service.MemberPointService;
import com.board.api.domain.member.service.MemberService;
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
    public ResponseEntity<SignUpResponse> doSignUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        long memberId = memberService.createMember(request);

        if (0L == memberId) {
            return ResponseEntity.badRequest()
                    .body(SignUpResponse.builder()
                            .resultCode(HttpStatus.BAD_REQUEST.value())
                            .resultMsg(HttpStatus.BAD_REQUEST.name())
                            .memberId(0L)
                            .build()
                    );
        }

        return ResponseEntity.ok()
                .body(SignUpResponse.builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMsg(HttpStatus.OK.name())
                        .memberId(memberId)
                        .build()
                );
    }

    @GetMapping("/point")
    public ResponseEntity<PointResponse> inquiryPoint() {
        Long point = memberPointService.getPoint();

        return ResponseEntity.ok()
                .body(PointResponse.builder()
                        .resultCode(HttpStatus.OK.value())
                        .resultMsg(HttpStatus.OK.name())
                        .point(point)
                        .build()
                );
    }

}
