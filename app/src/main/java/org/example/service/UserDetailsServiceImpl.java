package org.example.service;

import io.jsonwebtoken.security.Password;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.DTO.UserInfoDto;
import org.example.entities.UserInfo;
import org.example.eventProducer.UserInfoProducer;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;

@Service
@AllArgsConstructor
//it fetches the data from db and gives it to UserDetails
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       UserInfo user=userRepository.findByUsername(username);
       if(user==null){
           throw new UsernameNotFoundException("could not found user");
       }
       return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    @Transactional
    public Boolean signupUser(UserInfoDto userInfoDto){

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }
        //push event to kafka
        userInfoProducer.sendEventToKafka(userInfoDto);
        userRepository.save(new UserInfo(userInfoDto.getUsername(),userInfoDto.getPassword(),new HashSet<>()));
        return true;
    }

}
