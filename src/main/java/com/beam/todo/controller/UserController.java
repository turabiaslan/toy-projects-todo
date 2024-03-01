package com.beam.todo.controller;


import com.beam.todo.ToDoDto.AuthenticationResult;
import com.beam.todo.ToDoDto.LoginRequest;
import com.beam.todo.model.Profile;
import com.beam.todo.model.User;
import com.beam.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public static final String SESSION_USER = "session_user";

    @PostMapping("register")
    public AuthenticationResult register(@RequestBody User user,
                                        HttpSession session) {
        AuthenticationResult result = userService.register(user.getMailAddress(), user.getPassword());
        if(result.isSuccess()){
            session.setAttribute(SESSION_USER, result.getUser());
            return result;
        }else {
            return null;
        }
    }


    @PostMapping("login")
    public AuthenticationResult login(@RequestBody LoginRequest request,
                                      HttpSession session) {
        AuthenticationResult result = userService.login(request.getMailAddress(), request.getPassword());
        if (result.isSuccess()) {
            session.setAttribute(SESSION_USER, result.getUser());
        }
        return result;
    }

    @GetMapping("logout")
    public AuthenticationResult logout(HttpSession session) {
        session.removeAttribute(SESSION_USER);
        return new AuthenticationResult()
                .setSuccess(false);
    }


    @PostMapping("me")
    public AuthenticationResult updateProfile(@RequestParam String name,
                                              @RequestParam String surname,
                                              MultipartFile file,
                                              HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            userService.updateProfile(user, name, surname, file);
            return new AuthenticationResult()
                    .setUser(user)
                    .setProfile(user.getProfile())
                    .setSuccess(true);
        } else {
            return new AuthenticationResult()
                    .setSuccess(false);
        }
    }

    @GetMapping("me")
    public AuthenticationResult whoami(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user != null) {
            return new AuthenticationResult()
                    .setUser(user)
                    .setProfile(user.getProfile())
                    .setSuccess(true);
        } else {
            return new AuthenticationResult()
                    .setSuccess(false);
        }
    }



    @GetMapping("/avatar/{filename}")
    public ResponseEntity<byte[]> readAvatar(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        return ResponseEntity.ok(userService.readUserAvatar(user));
    }




}
