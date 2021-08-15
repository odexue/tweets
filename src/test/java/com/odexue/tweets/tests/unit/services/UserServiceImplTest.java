package com.odexue.tweets.tests.unit.services;

import com.odexue.tweets.entities.User;
import com.odexue.tweets.models.UserDto;
import com.odexue.tweets.repositories.UserRepository;
import com.odexue.tweets.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void registerUserTest() {
        var userDto = new UserDto();
        userDto.setUsername("sampleName");
        userDto.setPassword("samplePassword");

        var user = new User();
        var hashedPassword = "hashedPassword";
        user.setUsername(userDto.getUsername());
        user.setPassword(hashedPassword);

        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn(hashedPassword);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        userService.registerUser(userDto);

        Mockito.verify(passwordEncoder).encode(userDto.getPassword());
        Mockito.verify(userRepository).save(user);
    }


    @Test
    public void doesUserAlreadyExistTest() {
        UserDto userDto = new UserDto();
        userDto.setUsername("feila");
        var maybeUser  = Optional.of(TestUtil.createUser(1l, userDto.getUsername()));
        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(maybeUser);
        Assertions.assertTrue(userService.doesUserAlreadyExist(userDto));

        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        Assertions.assertFalse(userService.doesUserAlreadyExist(userDto));
    }


}
