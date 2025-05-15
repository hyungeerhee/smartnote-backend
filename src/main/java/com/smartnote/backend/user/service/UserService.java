package com.smartnote.backend.user.service;

import com.smartnote.backend.auth.dto.SignupRequestDto;
import com.smartnote.backend.auth.security.JwtUtil;
import com.smartnote.backend.auth.dto.LoginRequestDto;
import com.smartnote.backend.user.domain.User;
import com.smartnote.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setNickname(requestDto.getNickname());

        userRepository.save(user);
    }

    //로그인
    public String login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getEmail());
    }
}