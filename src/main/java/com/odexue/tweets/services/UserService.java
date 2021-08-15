package com.odexue.tweets.services;

import com.odexue.tweets.models.UserDto;

public interface UserService {

    void registerUser(UserDto userDto);
    boolean doesUserAlreadyExist(UserDto userDto);
}
