package com.board.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryPoint {
    private final String point;

    @Builder
    public InquiryPoint(Long point) {
        this.point = String.valueOf(point);
    }
}
