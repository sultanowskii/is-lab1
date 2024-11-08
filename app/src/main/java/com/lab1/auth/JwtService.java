package com.lab1.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.lab1.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${app.jwt-token-key}")
    private String jwtSigningKey;

    private static long jwtExpirationTime = 7 * 24 * 60 * 60 * 1000;

    // Gets data from token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractUsername(String token) {
        return (String) extractAllClaims(token).get("username");
    }

    public int extractId(String token) {
        return (int) extractAllClaims(token).get("id");
    }

    public String extractType(String token) {
        return (String) extractAllClaims(token).get("type");
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User u) {
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            claims.put("type", u.getType());
        }
        return generateToken(claims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> additionalData, UserDetails userDetails) {
        return Jwts.builder()
            .claims(additionalData)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSigningKey));
    }
}
