package com.khang.security;
<<<<<<< HEAD
// logic jwt tạo kiểm tra giải mã
import io.jsonwebtoken.Claims; //lấy data trong token
import io.jsonwebtoken.JwtException; // bắt lỗi jwt token sai, hét hạn bị sửa
import io.jsonwebtoken.Jwts;  // tạo token đọc token
import io.jsonwebtoken.security.Keys; // ký token
import org.springframework.stereotype.Component; // DI trong lệnh @

import javax.crypto.SecretKey; // lưu key bảo mật jwt
import java.nio.charset.StandardCharsets; // chuyển string thành byte dùng khi tạo key
import java.util.Date; // set hạn token

@Component  // tụ tạo bean
public class JwtService {
    private final SecretKey key;
    private final long validityMs = 24 * 60 * 60 * 1000L; // 1 day

    public JwtService() {
        String secret = "replace_this_with_a_long_strong_secret_at_least_32_chars";  // chuỗi gốc ban đầu
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));  //string -> aray byte-> tạo ra key  để ký token(sign,verify)
    }

    public String generate(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs); //toekn hết hạn sau 1 day

        return Jwts.builder()  // taọ token
                .subject(username)  //lưu username vào token
                .issuedAt(now)
                .expiration(exp)
                .signWith(key) //ký token chống giả maọ đảm bảo key token do sever tạo
                .compact(); // trả về string token
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);  //check chữ ký (có đúng key không), sửa k , hết hạn chưa
=======
// file xử lý token
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    // Constructor lấy chuỗi biến thành key để key ký jwt
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
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
<<<<<<< HEAD
                                                                          // lấy username từ token
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser() //libarry.đọc token
                .verifyWith(key)
                .build() //parser (máy soi)
                .parseSignedClaims(token) //đọc
                .getPayload(); // lấy data bên trong token
=======
    //Lấy username từ token
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
    }
}
