package com.voronkov.authserverforchat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.Calendar;
import java.util.Date;

@Log
public class JwtProviderImpl implements JwtProvider {

    @Value("$(jwt.secret)")
    private String jwtSecret;

    @Value("$(jwt.refresh.secret)")
    private String jwtRefreshSecret;

    private Calendar cal;

    public String generateAccessToken(String login) {
//        Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 30);
        cal.getTime();
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String login) {
//        Date date = Date.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.getTime();
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getLoginFromAccessToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getLoginFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
