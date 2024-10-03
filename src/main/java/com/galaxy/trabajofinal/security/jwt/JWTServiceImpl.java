package com.galaxy.trabajofinal.security.jwt;

import com.galaxy.trabajofinal.security.DTOs.LoginResponseDTO;
import com.galaxy.trabajofinal.security.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JWTServiceImpl implements IJWTService{
    @Override
    public LoginResponseDTO generateJwtToken(UserDetails userDetails) {
        log.info("Generando Token...");
        Collection<?> authorities=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities",authorities);
        String token = Jwts.builder().claims(claims).subject(userDetails.getUsername())
                .issuer("Trabajo_final").issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION_TIME))
                .signWith(getSigningSecretKey()).compact();
        return LoginResponseDTO.builder().token(token).build();
    }

    @Override
    public LoginResponseDTO generateJwtTokenWithUserId(UserDetails userDetails,Long userId) {
        log.info("Generando Token con Id...");
        Collection<?> authorities=userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities",authorities);
        claims.put("userId",userId);
        String token = Jwts.builder().claims(claims).subject(userDetails.getUsername())
                .issuer("Trabajo_final").issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION_TIME))
                .signWith(getSigningSecretKey()).compact();
        return LoginResponseDTO.builder().token(token).userId(userId).build();
    }

    @Override
    public String getUserNameFromJwtToken(String token) {
        return getAllClaims(token).getSubject();
    }

    @Override
    public Claims getAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public boolean validateJwtToken(String token) {
        log.info("token " + token);
        try {
            Jwts.parser().verifyWith(getSigningSecretKey()).build();
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public String getJwtToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
    private SecretKey getSigningSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.SUPER_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
