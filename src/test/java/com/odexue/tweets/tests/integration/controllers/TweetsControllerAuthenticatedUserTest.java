package com.odexue.tweets.tests.integration.controllers;


import com.odexue.tweets.controllers.TweetsController;
import com.odexue.tweets.models.TweetsDto;
import com.odexue.tweets.models.UserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("integration")
@Tag("integration")
@AutoConfigureMockMvc
@WithUserDetails(value="user1")
public class TweetsControllerAuthenticatedUserTest {

    @Autowired
    private MockMvc mvc;

    private final String FORWARD = "/";
    private final String TWEETS_PATH = "/tweets";
    private final String OTHER_TWEETS_PATH = "/otherTweets";
    private final String MY_TWEETS_PATH = "/myTweets";
    private final String CREATE_TWEETS_PATH = "/createTweets";
    private final String CREATE_TWEETS_ERROR_PATH = "/createTweetsError";
    private final String TWEETS_DTO_ATTR = "tweetsDto";
    private final String USER1 = "user1";
    private final String USER_DTO = "userDto";


    @Test
    public void createTweetsWithMaxLimitTweetsErrorTest() throws Exception {
        var sb = new StringBuilder();
        IntStream.range(0, 167).forEach(r -> {
            sb.append(r);
        });
        var maxLimitTweets = sb.toString();
        mvc.perform(post(CREATE_TWEETS_PATH)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("tweets", maxLimitTweets))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(CREATE_TWEETS_ERROR_PATH));

    }

    @Test
    public void createTweetsWithInvalidTweetsErrorTest() throws Exception {
        mvc.perform(post(CREATE_TWEETS_PATH)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("tweets", " "))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(CREATE_TWEETS_ERROR_PATH));

        String nullTweets = null;
        mvc.perform(post(CREATE_TWEETS_PATH)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("tweets", nullTweets))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(CREATE_TWEETS_ERROR_PATH));
    }

    @Test
    public void createTweetsWithFormTest() throws Exception {
        mvc.perform(post(CREATE_TWEETS_PATH)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("tweets", "hey tweets"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MY_TWEETS_PATH));
    }

    @Test
    public void createTweetsErrorTest() throws Exception {
        mvc.perform(get(CREATE_TWEETS_ERROR_PATH))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(TWEETS_PATH))
                .andExpect(model().attribute(TweetsController.CREATE_ON_ATTR, true))
                .andExpect(model().attribute(TweetsController.TWEET_ERR_ATTR, true))
                .andExpect(model().attributeExists(TWEETS_DTO_ATTR));
    }

    @Test
    public void createTweetsTest() throws Exception {
        mvc.perform(get(CREATE_TWEETS_PATH))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(TWEETS_PATH))
                .andExpect(model().attribute(TweetsController.CREATE_ON_ATTR, true))
                .andExpect(model().attributeExists(TWEETS_DTO_ATTR));
    }

    @Test
    public void myTweetsTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername(USER1);
        var tweetsListMatcher = Matchers.hasItems(Matchers.<TweetsDto> hasProperty(USER_DTO, Matchers.equalTo(userDto)));

        mvc.perform(get(MY_TWEETS_PATH))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(TWEETS_PATH))
                .andExpect(model().attributeExists(TweetsController.TWEETS_LIST_ATTR))
                .andExpect(model().attribute(TweetsController.TWEETS_LIST_ATTR, tweetsListMatcher));
    }

    @Test
    public void otherTweetsTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername(USER1);
        var tweetsListMatcher = Matchers.hasItems(Matchers.<TweetsDto> hasProperty(USER_DTO, Matchers.not(userDto)));

        mvc.perform(get(OTHER_TWEETS_PATH))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(TWEETS_PATH))
                .andExpect(model().attributeExists(TweetsController.TWEETS_LIST_ATTR))
                .andExpect(model().attribute(TweetsController.TWEETS_LIST_ATTR, tweetsListMatcher));
    }

    @Test
    public void tweetsTest() throws Exception {
        mvc.perform(get(TWEETS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(TweetsController.MAIN_VIEW))
                .andExpect(model().attribute(TweetsController.TWEETER_ATTR, USER1));
    }

    @Test
    public void redirectToTweetsTest() throws Exception {
        mvc.perform(get(FORWARD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TWEETS_PATH));
    }

}
