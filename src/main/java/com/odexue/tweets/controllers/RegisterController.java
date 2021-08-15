package com.odexue.tweets.controllers;


import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class RegisterController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @GetMapping("register")
    public String registerForm(UserDto userDto) {
        return "register.html";
    }

    @GetMapping("registerError")
    public String registerFormError(Model model, UserDto userDto) {
        model.addAttribute("registerErrorMessage", "Username already exists. Please use a different username.");
        model.addAttribute("registerError", true);
        return "register.html";
    }

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register.html";
        }
        var userExists = userServiceImpl.doesUserAlreadyExist(userDto);
        if (userExists) {
            return "redirect:/registerError";
        } else {
            userServiceImpl.registerUser(userDto);
            return "redirect:/signIn";
        }
    }


}
