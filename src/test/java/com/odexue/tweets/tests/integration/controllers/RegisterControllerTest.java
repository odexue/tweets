package com.odexue.tweets.tests.integration.controllers;


import com.odexue.tweets.controllers.RegisterController;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@SpringBootTest
@ActiveProfiles("integration")
@Tag("integration")
@AutoConfigureMockMvc
public class RegisterControllerTest {

    private final String REGISTER = "/register";
    private final String REGISTER_ERROR = "/registerError";

    @Autowired
    private MockMvc mvc;



    @Test
    public void registerWithFormErrorMaxLimit() throws Exception {
        var sb = new StringBuilder();
        IntStream.range(0, 277).forEach(r -> {
            sb.append(r);
        });
        var maxUsernamePassChars = sb.toString();
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", maxUsernamePassChars)
                        .param("password", "pass"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userDto"))
                .andExpect(view().name(RegisterController.REGISTER_VIEW));

        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", "user")
                        .param("password", maxUsernamePassChars))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userDto"))
                .andExpect(view().name(RegisterController.REGISTER_VIEW));
    }

    @Test
    public void registerWithFormErrorNotBlank() throws Exception {
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", " ")
                        .param("password", "pass"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userDto"))
                .andExpect(view().name(RegisterController.REGISTER_VIEW));
    }

    @Test
    public void registerWithFormErrorNotNull() throws Exception {
        String nullUsernameAndPassword = null;
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", nullUsernameAndPassword)
                        .param("password", nullUsernameAndPassword))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("userDto"))
                .andExpect(view().name(RegisterController.REGISTER_VIEW));
    }

    @Test
    public void registerExistingUserButWithSpaceTest() throws Exception {
        var existingUser = "user1 ";
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", existingUser)
                        .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registerError"));
    }

    @Test
    public void registerExistingUserTest() throws Exception {
        var existingUser = "user1";
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", existingUser)
                        .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registerError"));
    }

    @Test
    public void registerNewUserTest() throws Exception {
        var newUser = "user500";
        mvc.perform(post(REGISTER)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .with(csrf())
                        .param("username", newUser)
                        .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signIn"));
    }

    @Test
    public void registerErrorTest() throws Exception {
        mvc.perform(get(REGISTER_ERROR))
                .andExpect(status().isOk())
                .andExpect(view().name(RegisterController.REGISTER_VIEW))
                .andExpect(model().attribute(RegisterController.REGISTER_ERR_MSG_ATTR, RegisterController.REGISTER_ERROR_MSG))
                .andExpect(model().attribute(RegisterController.REGISTER_ERROR_ATTR, true));
    }

    @Test
    public void registerFormTest() throws Exception {
        mvc.perform(get(REGISTER))
                .andExpect(status().isOk())
                .andExpect(view().name(RegisterController.REGISTER_VIEW));
    }

}
