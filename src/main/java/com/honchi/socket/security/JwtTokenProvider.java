package com.honchi.socket.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    public boolean validationToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().get("type").equals("access_token");
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
