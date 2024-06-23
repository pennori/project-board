package com.board.api.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "email is a required input.")
    @Size(min = 1, max = 255, message = "userId size is between 1 and 255")
    private String email;

    @NotBlank(message = "password is a required input.")
    @Size(min = 1, max = 255, message = "password size is between 1 and 255")
    private String password;
}
