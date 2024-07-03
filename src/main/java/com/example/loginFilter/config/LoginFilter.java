package com.example.loginFilter.config;

import com.example.loginFilter.jwt.JwtUtil;
import com.example.loginFilter.member.model.CustomUserDetails;
import com.example.loginFilter.member.model.dto.MemberLoginReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        MemberLoginReq memberLoginReq;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            memberLoginReq = objectMapper.readValue(messageBody, MemberLoginReq.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = memberLoginReq.getEmail();
        String password = memberLoginReq.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        String email = userDetails.getUsername();
        Long id = userDetails.getId();

        //JwtUtil구현해서 위 정보 담아서 토큰 생성하기
        String token = jwtUtil.createToken(email, id);

        //생성한 토큰 응답 데이터에 담아서 응답
        PrintWriter out = response.getWriter();
        out.println("{\"isSuccess\": true, \"accessToken\":\""+token+"\"}");
        response.addHeader("Authorization", "Bearer " + token);
    }
}
