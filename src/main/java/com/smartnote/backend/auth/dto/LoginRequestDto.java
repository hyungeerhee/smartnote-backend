package com.smartnote.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "비밀번호", example = "password1234")
    private String password;
}