package com.odexue.tweets.tests.integration.controllers;

import com.odexue.tweets.controllers.LoginController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@SpringBootTest
@ActiveProfiles("integration")
@Tag("integration")
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    private final String SIGN_IN = "/signIn";
    private final String SIGN_IN_ERROR = "/signInError";
    private final String LOGOUT = "/logout";


    @Test
    @WithUserDetails(value="user1")
    public void logoutForAuthenticatedUserTest() throws Exception {
        mvc.perform(get(LOGOUT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SIGN_IN));
    }

    @Test
    public void logoutTest() throws Exception {
        mvc.perform(get(LOGOUT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SIGN_IN));
    }

    @Test
    public void signInErrorTest() throws Exception {
        mvc.perform(get(SIGN_IN_ERROR))
                .andExpect(status().isOk())
                .andExpect(view().name(LoginController.LOGIN_VIEW))
                .andExpect(model().attribute(LoginController.LOGIN_ERROR_ATTR, true));
    }

    @Test
    public void signInTest() throws Exception {
        mvc.perform(get(SIGN_IN))
                .andExpect(status().isOk())
                .andExpect(view().name(LoginController.LOGIN_VIEW));
    }


}
