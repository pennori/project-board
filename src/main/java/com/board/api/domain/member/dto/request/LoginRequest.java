package com.board.api.domain.member.dto.request;

import jakarta.validation.constraints.Email;
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
    @Email(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "invalid email")
    @Size(min = 1, max = 255, message = "email size is between 1 and 255")
    private String email;

    @NotBlank(message = "password is a required input.")
    @Size(min = 8, max = 16, message = "password size is between 8 and 16")
    private String password;
}
