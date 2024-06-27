package com.board.api.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "email 은 필수 값입니다.")
    @Email(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "유효하지 않은 email 입니다.")
    @Size(min = 1, max = 255, message = "email 은 1 ~ 255자 입니다.")
    private String email;

    @NotBlank(message = "password 은 필수 값입니다.")
    @Size(min = 8, max = 16, message = "password 은 8 ~ 16자 입니다.")
    private String password;

    @NotBlank(message = "name 은 필수 값입니다.")
    @Size(min = 1, max = 10, message = "name 은 1 ~ 10자 입니다.")
    private String name;

    @NotBlank(message = "regNo is a required input.")
    @Pattern(regexp = "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-8][0-9]{6}$", message = "유효하지 않은 regNo 입니다.")
    private String regNo;
}
