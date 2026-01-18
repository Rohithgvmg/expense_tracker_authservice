package org.example.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.example.entities.UserInfo;
import org.example.entities.UserRole;

import java.util.HashSet;
import java.util.Set;

//Dto are for internal app logic, not for db
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // converts snakeCase of request to camelCase and camelCase in app to snakeCase in response
@Getter
@Setter
public class UserInfoDto {

    @JsonProperty("username")
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private Long phoneNumber;

    private String email;

    private String profilePic;

    private Set<UserRole> roles;

}


