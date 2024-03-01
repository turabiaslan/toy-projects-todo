package com.beam.todo.repository;

import com.beam.todo.model.Priority;
import com.beam.todo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {



    User findByMailAddress(String mailAddress);

    boolean existsByMailAddress(String mailAddress);





}
