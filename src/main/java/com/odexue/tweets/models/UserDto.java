package com.odexue.tweets.models;

import com.odexue.tweets.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class  UserDto {

    @NotNull
    @NotBlank
    @Size(min=1, max = 255)
    String username;

    @NotNull
    @Size(min=1, max = 255)
    String password;

    public UserDto(User user) {
        this.username = user.getUsername();
    }

}
