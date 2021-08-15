package com.odexue.tweets.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {

    @GetMapping(value = "heyThere")
    public String hello() {
        return "hey there";
    }

}
