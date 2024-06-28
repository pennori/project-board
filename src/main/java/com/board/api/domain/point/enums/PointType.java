package com.board.api.domain.point.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    CREATE_POST(3L),
    DELETE_POST(-3L),
    CREATE_COMMENT(2L),
    DELETE_COMMENT(-2L),
    CREATE_BY(1L),
    DELETE_BY(-1L);
    private final Long score;
}
