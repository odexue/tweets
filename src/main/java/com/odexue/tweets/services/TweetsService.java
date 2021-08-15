package com.odexue.tweets.services;

import com.odexue.tweets.models.TweetsDto;
import com.odexue.tweets.models.UserDto;

import java.util.List;

public interface TweetsService {

    List<TweetsDto> showAllTweets();
    List<TweetsDto> showAllTweetsByUser(UserDto userDto);
    TweetsDto createTweets(UserDto user, String tweet);
    List<TweetsDto> showAllTweetsExceptByUser(UserDto userDto);

}
