package com.onetwo.myxservice.adapter.in.web.config;

import com.onetwo.myxservice.common.GlobalStatus;
import com.onetwo.myxservice.common.config.filter.AccessKeyCheckFilter;
import com.onetwo.myxservice.common.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest().permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public TokenProvider tokenProvider() {
        return new TokenProvider() {
            @Override
            public Authentication getAuthentication(String token) {
                return new UsernamePasswordAuthenticationToken(token, token);
            }

            @Override
            public Claims getClaimsByToken(String token) {
                return null;
            }

            @Override
            public boolean validateToken(String token) {
                return false;
            }
        };
    }

    @Bean
    @Primary
    public AccessKeyCheckFilter accessKeyCheckFilter() {
        return new TestAccessKeyCheckFilter();
    }

    class TestAccessKeyCheckFilter extends AccessKeyCheckFilter {
        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            filterChain.doFilter(request, response);
        }
    }

    class TestHeader {
        private static String secretKey;
        private static String accessId;
        private static String accessKey;

        @Value("${jwt.secret-key}")
        public void setSecretKey(String secretKey) {
            TestHeader.secretKey = secretKey;
        }

        @Value("${access-key}")
        public void setAccessId(String accessId) {
            TestHeader.accessId = accessId;
        }

        @Value("${access-key}")
        public void setAccessKey(String accessKey) {
            TestHeader.accessKey = accessKey;
        }

        public static HttpHeaders getRequestHeader() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(GlobalStatus.ACCESS_ID, accessId);
            httpHeaders.add(GlobalStatus.ACCESS_KEY, accessKey);
            return httpHeaders;
        }

        public static HttpHeaders getRequestHeaderWithMockAccessKey(String userId) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(GlobalStatus.ACCESS_ID, accessId);
            httpHeaders.add(GlobalStatus.ACCESS_KEY, accessKey);

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
            Key key = Keys.hmacShaKeyFor(encodedKey.getBytes());

            Date now = new Date();
            Date validity = new Date(now.getTime() + 100000);

            String token = Jwts.builder()
                    .setSubject(userId)
                    .setIssuedAt(now)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .setExpiration(validity)
                    .compact();

            httpHeaders.add(GlobalStatus.ACCESS_TOKEN, token);

            return httpHeaders;
        }
    }
}
