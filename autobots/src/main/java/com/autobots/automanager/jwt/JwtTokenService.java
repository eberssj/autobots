package com.autobots.automanager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  @Value("${jwt.expiration}")
  private long expiracao;

  public String createToken(String nomeUsuario) {
    Date expireTime = new Date(System.currentTimeMillis() + expiracao);

    return Jwts
      .builder()
      .setSubject(nomeUsuario)
      .setExpiration(expireTime)
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
      .compact();
  }

  public boolean validateToken(String jwtToken) {
    Claims claims = getClaims(jwtToken);
    if (claims != null) {
      String userEmail = claims.getSubject();
      Date expireTime = claims.getExpiration();
      Date now = new Date(System.currentTimeMillis());
      if (userEmail != null && expireTime != null && now.before(expireTime)) {
        return true;
      }
    }
    return false;
  }

  private Claims getClaims(String jwtToken) {
    try {
      return Jwts
        .parser()
        .setSigningKey(SECRET_KEY.getBytes())
        .parseClaimsJws(jwtToken)
        .getBody();
    } catch (Exception e) {
      return null;
    }
  }

  public String getUsername(String jwtToken) {
    Claims claims = getClaims(jwtToken);
    if (claims != null) {
      String username = claims.getSubject();
      return username;
    }
    return null;
  }
}