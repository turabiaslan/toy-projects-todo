package com.beam.sample.phinstagram.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {


    @RequestMapping({"/", "/home", "/login", "/register", "/user_settings" , "/profile/{username}", "/discover", "/search/{username}"})
    public String index() {return "index";}



    @RequestMapping({"/logout"})
    public String logout(HttpSession session) {
        session.removeAttribute(AccountController.SESSION_ACCOUNT);
        return index();
    }
}
