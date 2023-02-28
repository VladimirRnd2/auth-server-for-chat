package com.voronkov.authserverforchat.security;

public interface JwtProvider {

    String generateAccessToken(String login);

    String generateRefreshToken(String login);

    boolean validateToken(String token);

    boolean validateRefreshToken(String token);

    String getLoginFromAccessToken(String token);

    String getLoginFromRefreshToken(String token);
}
