package com.odexue.tweets.tests.unit.services;

import com.odexue.tweets.entities.User;

public class TestUtil {

    public static User createUser(Long id, String userName) {
        User user = new User();
        user.setId(id);
        user.setUsername(userName);
        user.setPassword("password");
        return user;
    }
}
