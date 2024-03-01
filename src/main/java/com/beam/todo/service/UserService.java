package com.beam.todo.service;


import com.beam.todo.ToDoDto.AuthenticationResult;
import com.beam.todo.model.Profile;
import com.beam.todo.model.User;
import com.beam.todo.repository.UserRepository;
import com.mongodb.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final ToDoService toDoService;

    private final DiskService diskService;


    public AuthenticationResult register(String mailAddress, String password) {
        if (userRepository.existsByMailAddress(mailAddress)) {
            return new AuthenticationResult().setMessage("The mail address you specified is already is in use");
        }
        if (password == null) {
            return new AuthenticationResult().setMessage("Password cannot be null");
        } else {
            User user = new User()
                    .setMailAddress(mailAddress)
                    .setPassword(passwordEncoder.encode(password));
            user.setId(UUID.randomUUID().toString());
            userRepository.save(user);
            return new AuthenticationResult().setUser(user).setProfile(user.getProfile()).setSuccess(true).setMessage("Welcome");
        }
    }

    public AuthenticationResult login(String mailAddress, String password) {
        User user = userRepository.findByMailAddress(mailAddress);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new AuthenticationResult().setUser(user).setProfile(user.getProfile()).setSuccess(true).setMessage("Welcome " + user.getProfile().getName());
        } else {
            return new AuthenticationResult().setMessage("E-mail address or password is not correct!");
        }
    }


    public AuthenticationResult logout(User user) {
        return new AuthenticationResult().setSuccess(false);
    }

    public Profile updateProfile(User user, String name, String surname, MultipartFile file){
        if(file != null){
            saveUserAvatar(file, user);
        }
        user.setProfile(user.getProfile().setName(name).setSurname(surname));
        userRepository.save(user);
        return user.getProfile();

    }

    public void saveUserAvatar(MultipartFile file, User user){
        String filename = "";
        try {
             filename = diskService.saveUserAvatar(file.getBytes(), user);
        }catch (IOException exc){
            log.error(exc.toString());
        }
        user.setProfile(user.getProfile().setAvatar("avatar/" + filename));
        userRepository.save(user);
    }

    public byte[] readUserAvatar(User user) {
        try {
            return diskService.readAvatar(user.getProfile().getAvatar());
        }catch (IOException exc) {
            log.error(exc.toString());
            return null;
        }
    }


}
