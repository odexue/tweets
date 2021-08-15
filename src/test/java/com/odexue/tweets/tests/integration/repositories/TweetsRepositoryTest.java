package com.odexue.tweets.tests.integration.repositories;


import com.odexue.tweets.entities.Tweets;
import com.odexue.tweets.entities.User;
import com.odexue.tweets.repositories.TweetsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This test uses data.sql to initialise the database tables
 */
@SpringBootTest
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Tag("integrationTest")
public class TweetsRepositoryTest {


    @Autowired
    private TweetsRepository tweetsRepository;

    @BeforeEach
    public void setupAndPrepare() {
        List<Tweets> tweetsFromDb = tweetsRepository.findAll();
        initialAssertions(tweetsFromDb);
    }

    @Test
    public void findByUserIdNotOrderByCreatedDateDescTest() {
        var userIdToExclude = 2l;
        List<Tweets> result = tweetsRepository.findByUserIdNotOrderByCreatedDateDesc(userIdToExclude);
        // tweets table contains 4
        Assertions.assertEquals(3, result.size());
        List<Long> userIds = result.stream().map(t -> t.getUser().getId()).collect(Collectors.toList());
        Assertions.assertFalse(userIds.contains(userIdToExclude));
    }

    @Test
    public void findByUserOrderByCreatedDateDescTest() {
        var user1Id = 1l;
        List<Tweets> tweetsFromDb = tweetsRepository.findByUserIdOrderByCreatedDateDesc(user1Id);

        Assertions.assertEquals(1, tweetsFromDb.size());
        Assertions.assertEquals(user1Id, tweetsFromDb.get(0).getUser().getId());

        var newTweet = "newTweetFromUser1";
        var tweets = createTweet(user1Id, newTweet);
        tweetsRepository.save(tweets);

        tweetsFromDb = tweetsRepository.findByUserIdOrderByCreatedDateDesc(user1Id);
        Assertions.assertEquals(2, tweetsFromDb.size());
        Assertions.assertEquals(user1Id, tweetsFromDb.get(0).getUser().getId());
        Assertions.assertEquals(newTweet, tweetsFromDb.get(0).getTweets());

        var notValidUser = 100l;
        tweetsFromDb = tweetsRepository.findByUserIdOrderByCreatedDateDesc(notValidUser);
        Assertions.assertEquals(0, tweetsFromDb.size());

    }

    @Test
    public void findByOrderByCreatedDateDescTest() {
        var message = "newTweetFromUser2";
        var tweets = createTweet(2l, message);
        tweetsRepository.save(tweets);
        List<Tweets> tweetsFromDb = tweetsRepository.findByOrderByCreatedDateDesc();
        Assertions.assertEquals(5, tweetsFromDb.size());
        Assertions.assertEquals(2, tweetsFromDb.get(0).getUser().getId());
    }

    private Tweets createTweet(Long userid, String tweet) {
        var tweets = new Tweets();
        tweets.setTweets(tweet);
        var user = new User();
        user.setId(userid);
        tweets.setUser(user);
        return tweets;
    }

    private void initialAssertions(List<Tweets> tweetsFromDb) {
        Assertions.assertEquals(4, tweetsFromDb.size());
        Assertions.assertEquals(1, tweetsFromDb.get(0).getUser().getId());
    }

}
