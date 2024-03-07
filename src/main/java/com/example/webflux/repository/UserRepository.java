package com.example.webflux.repository;

import com.example.webflux.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    // Create, Update
    Mono<User> save(User user); // 1개 유저 저장

    // Read
    Flux<User> findAll(); // 유저 찾기

    Mono<User> findById(Long id); // 단일 유저 찾기

    // Delete
    Mono<Integer> deleteById(Long id); // 유저 지우기

}
