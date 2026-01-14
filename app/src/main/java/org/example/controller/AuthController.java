package org.example.controller;



import org.example.entities.RefreshToken;
import org.example.DTO.UserInfoDto;
import org.example.response.JwtResponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController
{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        try {
            // 1. Validate the user isn't null before proceeding
            if (userInfoDto.getUserName() == null) {
                return new ResponseEntity<>("Username is required", HttpStatus.BAD_REQUEST);
            }

            // 2. Perform the signup
            Boolean isSignedUp = userDetailsService.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignedUp)) {
                return new ResponseEntity<>("User Already Exists", HttpStatus.BAD_REQUEST);
            }

            // 3. IMPORTANT: Use the username directly from the DTO
            // Ensure createRefreshToken is @Transactional
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUserName());

            String jwtToken = jwtService.GenerateToken(userInfoDto.getUserName());

            return new ResponseEntity<>(JwtResponseDTO.builder()
                    .accessToken(jwtToken)
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);

        } catch (Exception ex) {
            // Print the actual error to your console so you can see exactly what failed
            ex.printStackTrace();
            return new ResponseEntity<>("Exception in User Service: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}