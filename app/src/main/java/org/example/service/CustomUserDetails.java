package org.example.service;

//import lombok.AllArgsConstructor;
import org.example.entities.UserInfo;
import org.example.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//this class  fills the object with user information brought by UserServiceImplementation which can be used.
public class CustomUserDetails implements UserDetails {

    // UserInfo class has already necessary fields (can be extended)
    // UserDetails interface given by spring security
    // provides some useful methods

    private final String username;

    private final String password;

    //this constructor brings data from db, and fill these details in the class
    public CustomUserDetails(UserInfo userInfo){
        this.username=userInfo.getUsername();
        this.password=userInfo.getPassword();
        List<GrantedAuthority> auths=new ArrayList<>();

        for(UserRole role:userInfo.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }

        this.authorities=auths;

    }

    Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
