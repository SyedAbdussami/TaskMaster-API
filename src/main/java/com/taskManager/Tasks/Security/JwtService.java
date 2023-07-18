package com.taskManager.Tasks.Security;

import com.taskManager.Tasks.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY="V0/yZY9pFcefxdpiTVaN+zUwzzluCTzvQyZF8WTaR6UcZGbE1WxGRX6Ul3133yfm";

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey(){
        byte[]keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String,Object> extraClaims, User userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*12))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

    }
    public String generateToken(User userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String userName=extractUserName(token);
        return userName.equals(userDetails.getUsername()) && isTokenExpired(token);

    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

}
