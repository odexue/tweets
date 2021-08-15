package com.odexue.tweets.tests.unit.services;

import com.odexue.tweets.entities.Tweets;
import com.odexue.tweets.entities.User;
import com.odexue.tweets.exceptions.InvalidTweetLengthException;
import com.odexue.tweets.exceptions.InvalidUserException;
import com.odexue.tweets.exceptions.NoUserFoundException;
import com.odexue.tweets.models.TweetsDto;
import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.repositories.TweetsRepository;
import com.odexue.tweets.repositories.UserRepository;
import com.odexue.tweets.services.impl.TweetsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class TweetsServiceImplTest {

    @Mock
    private TweetsRepository tweetsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetsServiceImpl tweetsService;


    @Test
    public void createTweetsTest() {

        // must throw exception when
        Assertions.assertThrows(InvalidUserException.class, () -> {
            tweetsService.createTweets(null, null);
        });

        // must throw exception when
        Assertions.assertThrows(InvalidUserException.class, () -> {
            tweetsService.createTweets(null, "sometweet");
        });

        var user = TestUtil.createUser(2l, "user2");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        var validTweet = "validTweet";
        var tweets = createTweet(user.getId(), user.getPassword(), user.getUsername(), validTweet);
        tweets.setCreatedDate(null);
        Mockito.when(tweetsRepository.save(tweets)).thenReturn(tweets);
        var userDto = new UserDto(user.getUsername(), null);
        TweetsDto result = tweetsService.createTweets(userDto, validTweet);
        Assertions.assertEquals(validTweet, result.getTweets());
        Assertions.assertNotNull(result.getUserDto());

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        // must throw exception when
        Assertions.assertThrows(NoUserFoundException.class, () -> {
            tweetsService.createTweets(userDto, validTweet);
        });

        var sb = new StringBuilder();
        IntStream.range(0, 165).forEach(sb::append);
        // must throw exception when
        Assertions.assertThrows(InvalidTweetLengthException.class, () -> {
            tweetsService.createTweets(userDto, sb.toString());
        });

    }

    @Test
    public void convertTest() {
        var user = TestUtil.createUser(1l, "user");
        Assertions.assertNotNull(user.getPassword());
        var tweets = createTweet(user.getId(), user.getPassword(), user.getUsername(), "usertweet");
        var tweetsDto = tweetsService.convertToDto(tweets);
        Assertions.assertNotNull(tweetsDto.getTweets());
        Assertions.assertNotNull(tweetsDto.getUserDto());
        Assertions.assertNotNull(tweetsDto.getUserDto().getUsername());
        Assertions.assertNull(tweetsDto.getUserDto().getPassword());
        Assertions.assertNotNull(tweetsDto.getCreatedDate());
    }

    @Test
    public void validateTweetLengthTest() {
        var tweet = "validTweet";
        Assertions.assertTrue(tweetsService.isValidTweetLength(tweet));
        var sb = new StringBuilder();
        IntStream.range(0, 165).forEach(sb::append);
        var invalidTweet = sb.toString();
        Assertions.assertFalse(tweetsService.isValidTweetLength(invalidTweet));
        Assertions.assertFalse(tweetsService.isValidTweetLength(null));
    }

    @Test
    public void showAllTweetsExceptByUserTest() {

        // must throw exception when
        Assertions.assertThrows(InvalidUserException.class, () -> {
            tweetsService.showAllTweetsExceptByUser(null);
        });

        var userName1 = "user1";
        var userId1 = 1l;
        var userDto = new UserDto(userName1, null);
        Mockito.when(userRepository.findByUsername(userName1)).thenReturn(Optional.of(TestUtil.createUser(userId1, userName1)));
        var someTweets = tweetsFromDb();
        Mockito.when(tweetsRepository.findByUserIdNotOrderByCreatedDateDesc(userId1)).thenReturn(someTweets);
        var result2 = tweetsService.showAllTweetsExceptByUser(userDto);
        Assertions.assertEquals(someTweets.size(), result2.size());

        // must throw exception when
        Mockito.when(userRepository.findByUsername(userName1)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoUserFoundException.class, () -> {
            tweetsService.showAllTweetsExceptByUser(userDto);
        });
    }

    @Test
    public void showAllTweetsByUserTest() {
        // must throw exception when
        Assertions.assertThrows(InvalidUserException.class, () -> {
            tweetsService.showAllTweetsByUser(null);
        });

        var userName1 = "user1";
        var userId1 = 1l;
        var userDto = new UserDto(userName1, null);
        Mockito.when(userRepository.findByUsername(userName1)).thenReturn(Optional.of(TestUtil.createUser(userId1, userName1)));
        var someTweets = tweetsFromDb();
        Mockito.when(tweetsRepository.findByUserIdOrderByCreatedDateDesc(userId1)).thenReturn(someTweets);
        var result2 = tweetsService.showAllTweetsByUser(userDto);
        Assertions.assertEquals(someTweets.size(), result2.size());

        // must throw exception when
        Mockito.when(userRepository.findByUsername(userName1)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoUserFoundException.class, () -> {
            tweetsService.showAllTweetsByUser(userDto);
        });
    }

    @Test
    public void showAllTweetsTest() {
        var expectedList = tweetsFromDb();
        Mockito.when(tweetsRepository.findByOrderByCreatedDateDesc()).thenReturn(expectedList);
        List<TweetsDto> result = tweetsService.showAllTweets();
        Assertions.assertEquals(expectedList.size(), result.size());
        var sampleTweetsDto = result.get(0);
        Assertions.assertNotNull(sampleTweetsDto);
        Assertions.assertNotNull(sampleTweetsDto.getTweets());
        Assertions.assertNotNull(sampleTweetsDto.getUserDto().getUsername());
    }

    @Test
    public void createDtoListTest() {
        // must check that no password is pass in as result
        // must check that tweetsFromDb must still be equal to resulting dtoList
        var tweetsFromDb = tweetsFromDb();
        List<TweetsDto> result = tweetsService.createDtoList(tweetsFromDb);
        Assertions.assertEquals(tweetsFromDb.size(), result.size());
        var sampleTweetsDto = result.get(0);
        Assertions.assertNotNull(sampleTweetsDto.getUserDto().getUsername());
        Assertions.assertNull(sampleTweetsDto.getUserDto().getPassword());
    }

    private List<Tweets> tweetsFromDb() {
        var tweetsFromDb = new ArrayList<Tweets>();
        LongStream.range(0l, 5l).forEach(index -> {
            var tweets = createTweet(index, "pass", "username", "hey");
            tweetsFromDb.add(tweets);
        });
        return tweetsFromDb;
    }

    private Tweets createTweet(Long userId, String password, String userName, String tweet) {
        var user = new User();
        user.setId(userId);
        user.setPassword(password);
        user.setUsername(userName);
        var tweets = new Tweets();
        tweets.setUser(user);
        tweets.setTweets(tweet);
        tweets.setCreatedDate(Timestamp.from(Instant.now()));
        return tweets;
    }

}
