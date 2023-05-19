package com.kozarenko.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import static com.kozarenko.util.Constants.Auth.BEARER;

@Component
@PropertySource("classpath:application.properties")
public class JwtTokenUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expire.normal}")
  private Long expirationDefault;

  @Value("${jwt.expire.remember}")
  private Long expirationRemember;

  public boolean isTokenExists(String authHeader) {
    return authHeader != null && authHeader.startsWith(BEARER);
  }

  public String getUsernameFromToken(String authHeader) {
    return getClaimsFromToken(authHeader.startsWith(BEARER) ? authHeader.substring(BEARER.length())
            : authHeader, Claims::getSubject);

  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimsFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
  }

  public String generateToken(String username, boolean isToRemember) {
    Date now = new Date();

    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + (isToRemember ? expirationRemember : expirationDefault)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }
}