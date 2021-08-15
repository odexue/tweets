package com.odexue.tweets.services.impl;

import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.entities.User;
import com.odexue.tweets.repositories.UserRepository;
import com.odexue.tweets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registerUser(UserDto userDto) {
        var user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean doesUserAlreadyExist(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByUsername(userDto.getUsername().trim());
        if (maybeUser.isPresent()) {
            return true;
        } else {
            return false;
        }
    }


}
