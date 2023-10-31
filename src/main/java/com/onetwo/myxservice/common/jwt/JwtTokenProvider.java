package com.onetwo.myxservice.common.jwt;

import com.onetwo.myxservice.common.exceptions.TokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class JwtTokenProvider implements TokenProvider {

    private final String secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    private Key key;

    @PostConstruct
    public void init() throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
    
    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsByToken(token);
        String userId = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userId, token);
    }

    @Override
    public Claims getClaimsByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException(JwtCode.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            log.info("JwtException Token Denied : {}", e.getMessage());
            throw new TokenValidationException(JwtCode.ACCESS_TOKEN_DENIED);
        }
    }
}