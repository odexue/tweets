package com.odexue.tweets.repositories;

import com.odexue.tweets.entities.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetsRepository extends JpaRepository<Tweets, Long> {

    List<Tweets> findByOrderByCreatedDateDesc();
    List<Tweets> findByUserIdOrderByCreatedDateDesc(Long id);
    List<Tweets> findByUserIdNotOrderByCreatedDateDesc(Long id);

}
