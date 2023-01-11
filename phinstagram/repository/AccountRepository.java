package com.beam.sample.phinstagram.repository;

import com.beam.sample.phinstagram.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface AccountRepository extends MongoRepository <Account, String> {


    boolean existsByUsername(String username);

    Account findByUsername(String username);


    @Query("{'username': {$regex: ?0}}")
    List<Account> findByUsernameStartingWith(String regexp);

    //List<Account> findByUsernameList(String username);
}
