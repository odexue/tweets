package com.odexue.tweets.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {


    @GetMapping("signIn")
    public String login() {
        return "login.html";
    }

    @GetMapping("signInError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @GetMapping("logout")
    public String logout() {
        return "login.html";
    }


}
