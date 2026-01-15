package org.example.controller;



import org.example.entities.RefreshToken;
import org.example.request.AuthRequestDTO;
import org.example.request.RefreshTokenRequestDTO;
import org.example.response.JwtResponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


//refresh token endpoint is not accepting with headers but giving response
//
//login endpoint works only if the refresh token is expired , but the expired clean up of refresh token in db happens only on the call of function verifyExpiration
//


@RestController
public class TokenController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    //login endpoint is hit when both the access token and refresh token is expired
    @PostMapping("/auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        System.out.println("DTO Username: " + authRequestDTO.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            // if authentication manager verified you
            SecurityContextHolder.getContext().setAuthentication(authentication);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

            return new ResponseEntity<>(JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
           //both access token and refresh token is sent
        } else {
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //this endpoint is hit when access token is expired but refresh token is still alive
    @PostMapping("/auth/v1/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Error processing refresh token"));
    }

    @GetMapping("/auth/v1/home")
    public String getHomePage(){
        return "Home page ";
    }

}

