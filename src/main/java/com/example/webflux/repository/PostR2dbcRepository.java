package com.example.webflux.repository;

import com.example.webflux.model.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PostR2dbcRepository extends ReactiveCrudRepository<Post, Long>, PostCustomR2dbcRepository {

    Flux<Post> findByUserId(Long id);
}
