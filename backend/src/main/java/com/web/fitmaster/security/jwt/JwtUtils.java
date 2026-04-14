package com.web.fitmaster.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationInMs;
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    //Getting JWT from Header
    private final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);//log something into console
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;



    public String getJwtFromHeader(HttpServletRequest request) {
String bearerToken = request.getHeader("Authorization");
if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
    return bearerToken.substring(7);
}
return null;
}


    //Generating token from Username
    public String generateTokenFromUsername(UserDetails userDetails) {
    String username = userDetails.getUsername();
    return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date((new Date().getTime() + jwtExpirationInMs)))
            .signWith(key())
            .compact();
    }

    //Getting userName from JWT Token
    public String getPhoneFromJWTToken(String token) {
    return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    //Generating Signing Key
        public Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        }


    //Validate  JWT Token
    public boolean validateJwtToken(String token) {
    try{
       System.out.println("Validate");
       Jwts.
               parser().
               verifyWith((SecretKey) key()).
               build().
               parseSignedClaims(token);
       return true;
        }catch(MalformedJwtException e){
        logger.error("Malformed JWT token {}", e.getMessage());}
    catch (ExpiredJwtException e){
        logger.error("Expired JWT token {}", e.getMessage());
    }
    catch (UnsupportedJwtException e){
        logger.error("Unsupported JWT token {}", e.getMessage());
    }
    catch (IllegalArgumentException e){
        logger.error("JWT claims string is empty {}", e.getMessage());
    }
    return false;
    }




}
