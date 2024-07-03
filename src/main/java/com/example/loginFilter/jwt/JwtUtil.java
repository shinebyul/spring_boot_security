package com.example.loginFilter.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secretKey; //뭐하는 클래스지?

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey){ //lombok 라이블러리 말고 annotation 라이브러리
        //spring.jwt.secret 값을 받아서 secretKey애 담기

        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8)
                ,Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String createToken(String email, Long id){
        String token = Jwts.builder()
                .claim("username", email)
                .claim("id",id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .signWith(secretKey)
                .compact();
        return token;
    }
}
