package com.songnhip24.news.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component  // để inject vào class khác
public class JwtService {
    // tạo
    private final SecretKey key;
    // token hết hạn sau 8 tiếng
    private final long validityMs = 8 * 60 * 60 * 1000L;

    public JwtService(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        //Tạo SecretKey từ mảng byte = thuật toán HMAC-SHA(Chuyển String thành mảng byte[]) tính trù tượng ẩn chi tiết thuật toán
    }

    public String generate(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username) // gắn username
                .issuedAt(now)     // time phát hành token
                .expiration(new Date(now.getTime() + validityMs))  //thời gian hết hạn token sau 8h
                .signWith(key)  // tự tạo header và + Payload + SecretKey -> Signature
                .compact(); // gộp đống trên lại thành 1 string
    }
//validate
    private Claims parseClaims(String token) {  //Claims {sub:name,  iat:12313294 . exp:13421830} ->data in token
        return Jwts.parser()  //  tạo công cụ đọc JWT -> PARSER
                .verifyWith(key)  //Dùng secret key để verify signature
                .build()  //hoàn thành cấu hình tạo parser để dùng
                .parseSignedClaims(token)//Parse token + kiểm tra exp
                 .getPayload(); //lấy claims từ token
    }
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
// gửi username  vào controllẻ
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

}
