package com.michonski.football.security;

public interface SecurityConfiguration {
    String SECRET = "MateuszMichonskiSecretKey";
    long ACCESS_TOKEN_EXPIRATION_TIME = 180_000;
    long REFRESH_TOKEN_EXPIRATION_TIME = 180_000_000;
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_HEADER = "Authorization";
}
