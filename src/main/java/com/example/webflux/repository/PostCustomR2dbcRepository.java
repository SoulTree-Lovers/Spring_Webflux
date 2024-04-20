package com.example.webflux.repository;

import com.example.webflux.model.Post;
import reactor.core.publisher.Flux;

public interface PostCustomR2dbcRepository {

    Flux<Post> findAllByUserId(Long userId);

}
