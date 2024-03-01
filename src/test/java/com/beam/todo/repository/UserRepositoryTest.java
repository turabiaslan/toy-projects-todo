package com.beam.todo.repository;


import com.beam.todo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class UserRepositoryTest {


    @Autowired
    UserRepository userRepository;


    @Test
    public void  TestAddUser() {
        User user1 = new User()
                .setMailAddress("turabi.aslan@beamteknoloji.com")
                .setPassword("asdsads");
        user1.setId(UUID.randomUUID().toString());
        userRepository.save(user1);

    }


}
