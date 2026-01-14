package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
     public static final String SECRET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


     //Claims are key -value pairs inside jwt payload
    private Claims extractAllClaims(String token){
        //jwt is sent as string but has its own structure
        return Jwts.parser()   // jwt structure can be understood by Jwt parser object
                .verifyWith(getSignKey()) // checks the digital signature of the token with the secret key, if fails it throws an exception
                .build() // creates the empty object to store the fields
                .parseSignedClaims(token) // fills the header, payload and signature to the relevant fields
                .getPayload(); // returns the body of jwt which is JSON type which generally includes the username , expiration etc
    }

    // this function is generic so that it can be used for different purposes
    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        // Function is a type of functional interface
        // claimResolver takes Claims as input and returns T as output
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

     public String extractUsername(String token){
        // returns the value for the key "sub" in the claims
         // use key as sub in payload, instead of username , so, it works here
         return extractClaim(token, Claims::getSubject);

     }


     public Date extractExpiration(String token){
         return extractClaim(token,Claims::getExpiration);
     }




     private Boolean isTokenExpired(String token){
         return extractExpiration(token).before(new Date());
     }

     public Boolean validateToken(String token, UserDetails userDetails){
        // we must fill the userDetails object by some service from database before reaching here (I am thinking of CustomUserDetails)
         final String username=extractUsername(token);
         return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
     }

     private String createToken(Map<String,Object> claims, String username){
         return Jwts.builder()
                 .setClaims(claims)
                 .setSubject(username)
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis()+1000*60*1)) // 60 seconds
                 .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
     }

     private SecretKey getSignKey(){
        // secret string  -> key bytes -> Secret Key instance needed for signing with hmac algorithm
         byte[] keyBytes=Decoders.BASE64.decode(SECRET);
         return Keys.hmacShaKeyFor(keyBytes);
     }

    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

}
