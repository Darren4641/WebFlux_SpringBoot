package com.webflux.security.component;

import com.webflux.security.dto.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AuthTokenProvider {
    @Value("${jwt.access.key}")
    private String key;
    @Value("${jwt.access.validtime}")
    private Long accessTokenValidTime;
    private final String AUTHORITIES_KEY = "roles";
    private SecretKey secretKey;

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @PostConstruct
    public void init() {
        byte[] keyBytes = key.getBytes();
        this.secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }


    public String getAccessToken(ServerHttpRequest request) {
        String headerValue = request.getHeaders().getFirst(HEADER_AUTHORIZATION);

        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public validateToken()
    public String createToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, List.of(RoleType.USER.getRole()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(new Date(new Date().getTime() + accessTokenValidTime))
                .compact();
    }

    public String createToken(String id, List<String> roles) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, roles)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(new Date(new Date().getTime() + accessTokenValidTime))
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getTokenClaims(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(getRoleName(claims))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Boolean validateToken(String token) {
        return !Objects.requireNonNull(getTokenClaims(token)).isEmpty();
    }

    private Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    private String[] getRoleName(Claims claims) {
        String roles = (String)claims.get(AUTHORITIES_KEY).toString();
        roles = roles.replace("[", "").replace("]", "");
        return roles.split(",");
    }
}
