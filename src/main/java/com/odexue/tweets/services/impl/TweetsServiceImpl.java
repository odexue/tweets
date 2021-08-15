package com.odexue.tweets.services.impl;

import com.odexue.tweets.entities.Tweets;
import com.odexue.tweets.entities.User;
import com.odexue.tweets.exceptions.InvalidTweetLengthException;
import com.odexue.tweets.exceptions.InvalidUserException;
import com.odexue.tweets.exceptions.NoUserFoundException;
import com.odexue.tweets.models.TweetsDto;
import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.repositories.TweetsRepository;
import com.odexue.tweets.repositories.UserRepository;
import com.odexue.tweets.services.TweetsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TweetsServiceImpl implements TweetsService {

    @Autowired
    private TweetsRepository tweetsRepository;

    @Autowired
    private UserRepository userRepository;


    // TODO : this can be cached. can use @Cacheable from spring
    @Override
    public List<TweetsDto> showAllTweets() {
        log.info("Retrieving all tweets");
        List<Tweets> tweetsFromDb = tweetsRepository.findByOrderByCreatedDateDesc();
        return createDtoList(tweetsFromDb);
    }

    // TODO : this can be cached. can use @Cacheable from spring
    @Override
    public List<TweetsDto> showAllTweetsByUser(UserDto userDto) {
        if (!isValid(userDto)) {
            throw new InvalidUserException(InvalidUserException.DEFAULT_MESSAGE);
        } else {
            Optional<User> maybeUser = findUserByName(userDto);
            if (maybeUser.isPresent()) {
                var userId = maybeUser.get().getId();
                List<Tweets> tweetsFromDb = tweetsRepository.findByUserIdOrderByCreatedDateDesc(userId);
                return createDtoList(tweetsFromDb);
            } else {
                throw new NoUserFoundException(NoUserFoundException.DEFAULT_MESSAGE);
            }
        }
    }

    // TODO : this can be cached. can use @Cacheable from spring
    @Override
    public List<TweetsDto> showAllTweetsExceptByUser(UserDto userDto) {
        if (!isValid(userDto)) {
            throw new InvalidUserException(InvalidUserException.DEFAULT_MESSAGE);
        } else {
            Optional<User> maybeUser = findUserByName(userDto);
            if (maybeUser.isPresent()) {
                var userId = maybeUser.get().getId();
                List<Tweets> tweetsFromDb = tweetsRepository.findByUserIdNotOrderByCreatedDateDesc(userId);
                return createDtoList(tweetsFromDb);
            } else {
                throw new NoUserFoundException(NoUserFoundException.DEFAULT_MESSAGE);
            }
        }
    }

    @Override
    @Transactional
    public TweetsDto createTweets(UserDto userDto, String tweet) {
        if(!isValid(userDto)) {
            throw new InvalidUserException(InvalidUserException.DEFAULT_MESSAGE);
        } else if (!isValidTweetLength(tweet)) {
            throw new InvalidTweetLengthException(InvalidTweetLengthException.DEFAULT_MESSAGE);
        } else {
            Optional<User> maybeUser = findUserByName(userDto);
            if (maybeUser.isPresent()) {
                Tweets tweets = new Tweets();
                tweets.setUser(maybeUser.get());
                tweets.setTweets(tweet);
                Tweets createdTweet = tweetsRepository.save(tweets);
                return convertToDto(createdTweet);
            } else {
                throw new NoUserFoundException(NoUserFoundException.DEFAULT_MESSAGE);
            }
        }
    }

    public TweetsDto convertToDto(Tweets tweets) {
        return new TweetsDto(new UserDto(tweets.getUser()), tweets.getTweets(), tweets.getCreatedDate());
    }

    private Optional<User> findUserByName(UserDto userDto) {
        return userRepository.findByUsername(userDto.getUsername());
    }

    public boolean isValidTweetLength(String tweets) {
        if ((null == tweets) || (null != tweets && tweets.toCharArray().length > 160)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValid(UserDto userDto) {
        if (null == userDto || null == userDto.getUsername()) {
            return false;
        } else {
            return true;
        }
    }

    public List<TweetsDto> createDtoList(List<Tweets> tweetsFromDb) {
        var tweetsDtoList = new ArrayList<TweetsDto>();
        for (var element : tweetsFromDb) {
            var tweets = new TweetsDto(new UserDto(element.getUser()), element.getTweets(), element.getCreatedDate());
            tweetsDtoList.add(tweets);
        }
        return tweetsDtoList;
    }

}
