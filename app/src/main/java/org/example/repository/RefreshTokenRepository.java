package org.example.repository;


import org.example.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
      // above signature says that RefreshToken is entity ,we are dealing with
    // Integer is the primary key of that entity


       Optional<RefreshToken> findByToken(String token);
       // above method is converted to query by JPA as, in the table, find the column with name token (passed in argument)
      //  return the entire row of the table
      void deleteByUserInfo_UserId(Long userId);
       // the column name should match with the string after findBy
       // if the column is username , method should be findByUsername
}
