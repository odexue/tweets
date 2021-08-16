package com.odexue.tweets.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    public static final String LOGIN_VIEW = "login.html";
    public static final String LOGIN_ERROR_ATTR = "loginError";

    @GetMapping("signIn")
    public String login() {
        return LOGIN_VIEW;
    }

    @GetMapping("signInError")
    public String loginError(Model model) {
        model.addAttribute(LOGIN_ERROR_ATTR, true);
        return LOGIN_VIEW;
    }

//    @GetMapping("logout")
//    public String logout() {
//        return "login.html";
//    }


}
