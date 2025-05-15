package com.smartnote.backend.auth.security;

import com.smartnote.backend.user.domain.User;
import com.smartnote.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 스프링이 관리하는 Bean으로 등록
@RequiredArgsConstructor // final 필드(JwtUtil, UserRepository)를 자동 주입
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 "Authorization" 값을 꺼냄
        String authHeader = request.getHeader("Authorization");

        // 2. Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면, 다음 필터로 넘김
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " 다음의 실제 토큰만 잘라냄
        String token = authHeader.substring(7);

        // 4. 토큰에서 이메일(subject)을 추출함
        String email = jwtUtil.getEmailFromToken(token);

        // 5. 아직 인증 정보가 없고, 이메일이 존재하면
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. DB에서 사용자 조회
            User user = userRepository.findByEmail(email).orElse(null);

            // 7. 유효한 사용자이고, 토큰도 유효한지 검사
            if (user != null && jwtUtil.validateToken(token)) {
                // 8. 인증 객체 생성 → 스프링 시큐리티에 등록
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 9. 다음 필터로 넘어감 (계속 진행)
        filterChain.doFilter(request, response);
    }
}