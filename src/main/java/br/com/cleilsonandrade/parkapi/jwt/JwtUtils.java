package br.com.cleilsonandrade.parkapi.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
  public static final String JWT_BEARER = "Bearer";
  public static final String JWT_AUTHENTICATION = "Authorization";
  public static final String SECRET_KEY = "0123456789-0123456789-0123456789";

  public static final long EXPIRES_DAYS = 0;
  public static final long EXPIRES_HOURS = 0;
  public static final long EXPIRES_MINUTES = 2;

  private JwtUtils() {
  }

  private static Key generateKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }

  private static Date toExpireDate(Date start) {
    LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime end = dateTime.plusDays(EXPIRES_DAYS).plusHours(EXPIRES_HOURS).plusMinutes(EXPIRES_MINUTES);

    return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static JwtToken createToken(String username, String role) {
    Date issuedAt = new Date();
    Date limit = toExpireDate(issuedAt);

    String token = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setSubject(username)
        .setIssuedAt(issuedAt)
        .setExpiration(limit)
        .signWith(generateKey(), SignatureAlgorithm.HS256)
        .claim("role", role)
        .compact();

    return new JwtToken(token);
  }

  private static Claims getClaimsFromToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(generateKey()).build()
          .parseClaimsJws(refactorToken(token)).getBody();
    } catch (JwtException e) {
      log.error(String.format("Token invalid %s", e.getMessage()));
    }

    return null;
  }

  public static String getUsernameFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
  }

  public static boolean isTokenValid(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(generateKey()).build()
          .parseClaimsJws(refactorToken(token));

      return true;
    } catch (JwtException e) {
      log.error(String.format("Token invalid %s", e.getMessage()));
    }

    return false;
  }

  private static String refactorToken(String token) {
    if (token.contains(JWT_BEARER)) {
      return token.substring(JWT_BEARER.length());
    }
    return token;
  }
}
