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

    public static final String REGISTER_VIEW = "register.html";
    public static final String REGISTER_ERR_MSG_ATTR = "registerErrorMessage";
    public static final String REGISTER_ERROR_MSG = "Username already exists. Please use a different username.";
    public static final String REGISTER_ERROR_ATTR = "registerError";

    @Autowired
    private UserServiceImpl userServiceImpl;


    @GetMapping("register")
    public String registerForm(UserDto userDto) {
        return REGISTER_VIEW;
    }

    @GetMapping("registerError")
    public String registerFormError(Model model, UserDto userDto) {
        model.addAttribute(REGISTER_ERR_MSG_ATTR, REGISTER_ERROR_MSG);
        model.addAttribute(REGISTER_ERROR_ATTR, true);
        return REGISTER_VIEW;
    }

    @PostMapping(value = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
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
