package com.tasks.socialMediaApp.jwt;

import com.tasks.socialMediaApp.model.RefreshToken;
import com.tasks.socialMediaApp.model.User;
import com.tasks.socialMediaApp.repositories.UserRepository;
import com.tasks.socialMediaApp.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.secret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.refreshTokenExpirationMin}")
    private int refreshTokenExpirationMin;

    UserService userService;

    @Autowired
    JwtUtils(@Lazy UserService userService){
        this.userService = userService;
    }

    public String getJwtFromHeader(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");
        logger.debug("bearer Token: " + bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(String userName){

        return Jwts.builder()
                .subject(userName)
                .claim("roles", "ROLE_USER")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

   private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(jwtSecret.getBytes())));
   }

   public void validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
        }catch (MalformedJwtException exception){
            logger.error("Invalid JWT token: {}"+ exception.getMessage());
            throw exception;
        }
        catch (ExpiredJwtException exception){
            logger.error(" JWT token is expired: {}"+ exception.getMessage());
            throw exception;
        }catch (UnsupportedJwtException exception){
            logger.error("JWT token is unsupported: {}"+ exception.getMessage());
            throw exception;
        }catch (IllegalArgumentException exception){
            logger.error("JWT claims string is empty: {}"+ exception.getMessage());
            throw exception;
        }
   }

   public String generateRefreshTokenFromUserName(String userName){

       RefreshToken refreshToken = new RefreshToken();
               refreshToken.setRefreshToken(String.valueOf(UUID.randomUUID()));
               refreshToken.setExpiryTime(new Date((new Date()).getTime() + refreshTokenExpirationMin * 60000L));
       userService.saveRefreshToken(userName,refreshToken);

       return refreshToken.getRefreshToken();
   }

   public boolean checkExpiryOfRefreshToken(User user){

        return user.getRefreshToken().getExpiryTime().compareTo(Date.from(Instant.now())) < 0;
   }

   public void setNewExpiryTime(User user){

        user.getRefreshToken().setExpiryTime(new Date((new Date()).getTime() + refreshTokenExpirationMin * 60000L));
   }
}
