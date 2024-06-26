package com.board.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentPointDto {
    private final String point;

    @Builder
    public CurrentPointDto(Long point) {
        this.point = String.valueOf(point);
    }
}
