package com.smartnote.backend.user.controller;

import com.smartnote.backend.auth.dto.SignupRequestDto;
import com.smartnote.backend.auth.dto.LoginRequestDto;
import com.smartnote.backend.user.domain.User;
import com.smartnote.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원가입, 로그인 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임을 입력받아 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 인증 후 JWT 토큰을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        String token = userService.login(dto);
        response.setHeader("Authorization", "Bearer " + token);
        return ResponseEntity.ok("로그인 성공");
    }
}