package com.odexue.tweets.security;

import com.odexue.tweets.entities.User;
import com.odexue.tweets.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TweetsUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isPresent()) {
            return new TweetsUserDetails(maybeUser.get().getUsername(), maybeUser.get().getPassword());
        } else {
            throw new UsernameNotFoundException("User does not exist in the database");
        }
    }
}
