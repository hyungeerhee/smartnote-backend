package com.smartnote.backend;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        String token = userService.login(dto);

        // JWT 토큰을 응답 헤더에 담아 전달
        response.setHeader("Authorization", "Bearer " + token);

        return ResponseEntity.ok("로그인 성공");
    }

    //jwt 인증 테스트용
    @GetMapping("/api/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("안녕하세요, " + user.getNickname() + "님!");
    }




}
