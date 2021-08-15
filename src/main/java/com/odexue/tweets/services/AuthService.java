package com.odexue.tweets.services;

import com.odexue.tweets.security.TweetsUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (TweetsUserDetails) authentication.getPrincipal();
        if (null != userDetails) {
            return userDetails.getUsername();
        } else {
            throw new RuntimeException("No authenticated Principal is found");
        }
    }
}
