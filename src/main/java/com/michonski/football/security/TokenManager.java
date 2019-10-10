package com.michonski.football.security;

import com.michonski.football.dto.security.Tokens;
import com.michonski.football.exception.AppException;
import com.michonski.football.service.SecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public interface TokenManager {

    static Tokens generateTokens(Authentication authentication) {

        if ( authentication == null ) {
            throw new AppException("authentication object is null");
        }

        return Tokens.builder()
                .accessToken(createToken(authentication, TokenType.ACCESS_TOKEN))
                .refreshToken(createToken(authentication, TokenType.REFRESH_TOKEN))
                .build();
    }

    private static String createToken( Authentication authentication, TokenType tokenType ) {

        String username = authentication.getName();

        String roles = rolesAsString(authentication);
        if ( tokenType.equals(TokenType.REFRESH_TOKEN) ) {
            roles = "ROLE_REFRESH," + roles;
        }

        long expirationTime = System.currentTimeMillis() + SecurityConfiguration.ACCESS_TOKEN_EXPIRATION_TIME;
        if ( tokenType.equals(TokenType.REFRESH_TOKEN) ) {
            expirationTime = System.currentTimeMillis() + SecurityConfiguration.REFRESH_TOKEN_EXPIRATION_TIME;
        }

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(expirationTime))
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, SecurityConfiguration.SECRET)
                .compact();
    }

    private static String createTokenFromRefreshToken(String refreshToken) {

        if (refreshToken == null) {
            throw new AppException("refresh token is null");
        }

        String username = getUsername(refreshToken);
        String roles = getRoles(refreshToken).stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(","));
        long expirationTime = System.currentTimeMillis() + SecurityConfiguration.ACCESS_TOKEN_EXPIRATION_TIME;

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(expirationTime))
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, SecurityConfiguration.SECRET)
                .compact();
    }

    private static String rolesAsString( Authentication authentication ) {
        return authentication.getAuthorities()
                .stream()
                .map(role -> ((GrantedAuthority) role).getAuthority())
                .collect(Collectors.joining(","));
    }

    private static Claims getClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(SecurityConfiguration.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    static UsernamePasswordAuthenticationToken parseToken(String token) {
        if (token == null) {
            throw new AppException("parse token exception - token object is null");
        }
        if (!token.startsWith(SecurityConfiguration.TOKEN_PREFIX)) {
            throw new AppException("parse token exception - incorrect token");
        }

        String tokenToParse = token.replace(SecurityConfiguration.TOKEN_PREFIX, "");

        if (getExpirationDate(tokenToParse).before(new Date())) {
            throw new AppException("parse token exception - token has been expired");
        }

        return new UsernamePasswordAuthenticationToken(getUsername(tokenToParse), null, getRoles(tokenToParse));
    }

    static Tokens parseRefreshToken(String refreshToken) {
        return Tokens.builder()
                .accessToken(createTokenFromRefreshToken(refreshToken))
                .refreshToken(refreshToken)
                .build();
    }

    private static String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private static Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    private static List<GrantedAuthority> getRoles(String token) {
        return Arrays.stream(getClaims(token)
                .get("roles")
                .toString()
                .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
