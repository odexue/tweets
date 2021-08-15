package com.odexue.tweets.repositories;

import com.odexue.tweets.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

     Optional<User> findByUsername(String username);

}
