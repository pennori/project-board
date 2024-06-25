package com.board.api.domain.member.dto.response;

import com.board.api.global.dto.response.AbstractResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class PointResponse extends AbstractResponse {
    private final String point;

    @Builder
    public PointResponse(int resultCode, String resultMsg, Long point) {
        super(resultCode, resultMsg);
        this.point = String.valueOf(point);
    }
}
