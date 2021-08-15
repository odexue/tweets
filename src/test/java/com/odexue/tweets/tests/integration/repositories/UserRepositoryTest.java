package com.odexue.tweets.tests.integration.repositories;


import com.odexue.tweets.entities.User;
import com.odexue.tweets.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

/**
 * This test uses data.sql to initialise the database tables
 */
@SpringBootTest
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Tag("integrationTest")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameTest() {
        var user1 = "user1";
        Optional<User> result = userRepository.findByUsername(user1);
        Assertions.assertTrue(result.isPresent());
        User user = result.get();
        Assertions.assertEquals(user1, user.getUsername());

        var userThatDoestExist = "feila";
        Optional<User> result2 = userRepository.findByUsername(userThatDoestExist);
        Assertions.assertTrue(result2.isEmpty());
    }
}
