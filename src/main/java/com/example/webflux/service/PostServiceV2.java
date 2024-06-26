package com.example.webflux.service;

import com.example.webflux.model.Post;
import com.example.webflux.repository.PostR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PostServiceV2 {

    private final PostR2dbcRepository postR2dbcRepository;

    // Create
    public Mono<Post> create(Long userId, String title, String content) {
        return postR2dbcRepository.save(
            Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build()
        );
    }

    // Read
    public Flux<Post> findAll() {
        return postR2dbcRepository.findAll();
    }

    public Mono<Post> findById(Long id) {
        return postR2dbcRepository.findById(id);
    }

    public Flux<Post> findAllByUserId(Long id) {
        return postR2dbcRepository.findAllByUserId(id);
    }

    // Delete
    public Mono<Void> deleteById(Long id) {
        return postR2dbcRepository.deleteById(id);
    }

}
