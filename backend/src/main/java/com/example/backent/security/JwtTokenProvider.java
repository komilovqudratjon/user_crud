package com.example.backent.security;

import com.example.backent.entity.User;
import com.example.backent.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Autowired
    UserRepository userRepository;

    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        Date expiryDate = new Date(new Date().getTime() + jwtExpirationInMs);

//        User user=userRepository.getOne(userPrincipal.getId());
//        ReqToken reqToken=new ReqToken();
//        reqToken.setId(userPrincipal.getId());
//        reqToken.setEmail(userPrincipal.getEmail());
//        reqToken.setRole(user.getRoles());

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .setAudience(userPrincipal.getRoles().get(0).getName().toString())
                .setIssuer(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
