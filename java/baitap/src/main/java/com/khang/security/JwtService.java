package com.khang.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    // simple hardcoded secret for learning - replace with env in real project
    private final Key key;
    private final long validityMs = 24 * 60 * 60 * 1000L; // 1 day

    public JwtService() {
        String secret = "replace_this_with_a_long_strong_secret_at_least_32_chars";
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }
// tạo JWT khi user login
    public String generate(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }
//  cheek token hợp lệ khi user gửi request
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
    //Lấy username từ token
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
