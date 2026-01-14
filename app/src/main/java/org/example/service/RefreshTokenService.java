package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired UserRepository userRepository;

    @Transactional
    //transactional ensures if save method fails entire process rolls back
    public RefreshToken createRefreshToken(String username){
        UserInfo userInfoExtracted=userRepository.findByUsername(username);



        if(userInfoExtracted==null){
            System.out.println("user not found ");
            return null;
        }
        RefreshToken refreshToken=RefreshToken.builder()
                .userInfo(userInfoExtracted) // passes userInfo because refreshToken table needs UserInfo (so that we know which user owns this token)
                .token(UUID.randomUUID().toString())// token is a random hash
                .expiryDate(Instant.now().plusMillis(600000))//10 minutes
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh token is expired. Please make a new login");
        }
        return token;
    }
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }



}

