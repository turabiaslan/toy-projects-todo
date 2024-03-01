package com.beam.todo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {


    @RequestMapping({"/", "/home", "/login", "/register", "/profile","/starred", "/today", "/completed",
            "/month", "/planned", "search/{param}"})
    public String index() {
        return "index";
    }


    @RequestMapping({"logout"})
    public String logout(HttpSession session) {
        session.removeAttribute(UserController.SESSION_USER);
        return index();
    }


}
