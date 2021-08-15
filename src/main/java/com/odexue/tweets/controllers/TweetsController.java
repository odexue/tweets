package com.odexue.tweets.controllers;


import com.odexue.tweets.models.TweetsDto;
import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.services.AuthService;
import com.odexue.tweets.services.TweetsService;
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
public class TweetsController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TweetsService tweetsService;


    @GetMapping("/")
    public String redirect() {
        return "redirect:/tweets";
    }

    @GetMapping("tweets")
    public String mainPage(Model model, TweetsDto tweetsDto) {
        String tweeter = authService.getCurrentUsername();
        model.addAttribute("tweeterName", tweeter);
        return "main.html";
    }

    @GetMapping("otherTweets")
    public String otherTweets(Model model) {
        String currentUser = authService.getCurrentUsername();
        UserDto userDto = new UserDto();
        userDto.setUsername(currentUser);
        var tweets = tweetsService.showAllTweetsExceptByUser(userDto);
        model.addAttribute("tweetsList", tweets);
        return "forward:/tweets";
    }

    @GetMapping("myTweets")
    public String myTweets(Model model) {
        String currentUser = authService.getCurrentUsername();
        var userDto = new UserDto();
        userDto.setUsername(currentUser);
        var tweets = tweetsService.showAllTweetsByUser(userDto);
        model.addAttribute("tweetsList", tweets);
        return "forward:/tweets";
    }

    @GetMapping("createTweets")
    public String createTweetsForm(TweetsDto tweetsDto, Model model) {
        model.addAttribute("isCreateOn", true);
        return "forward:/tweets";
    }

    @GetMapping("createTweetsError")
    public String createTweetsError(TweetsDto tweetsDto, Model model, BindingResult bindingResult) {
        model.addAttribute("isCreateOn", true);
        model.addAttribute("tweetError", true);
        return "forward:/tweets";
    }

    @PostMapping(value = "createTweets", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createTweet(@Valid TweetsDto tweetsDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/createTweetsError";
        }
        var userDto = new UserDto();
        userDto.setUsername(authService.getCurrentUsername());
        tweetsService.createTweets(userDto, tweetsDto.getTweets());
        return "redirect:/myTweets";
    }
}
