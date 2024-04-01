package com.example.webflux.controller;

import com.example.webflux.controller.dto.PostCreateRequest;
import com.example.webflux.controller.dto.PostResponseV2;
import com.example.webflux.service.PostServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/posts")
public class PostControllerV2 {

    private final PostServiceV2 postServiceV2;

    @PostMapping("")
    public Mono<PostResponseV2> createPost(
        @RequestBody PostCreateRequest postCreateRequest
    ) {
        return postServiceV2.create(
            postCreateRequest.getUserId(),
            postCreateRequest.getTitle(),
            postCreateRequest.getContent()
        ).map(PostResponseV2::of);
    }

    @GetMapping("/find-all")
    public Flux<PostResponseV2> findAllPost() {
        return postServiceV2.findAll()
            .map(PostResponseV2::of);
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<PostResponseV2>> findPost(
        @PathVariable Long id
    ) {
        return postServiceV2.findById(id)
            // 찾는 포스트잇이 있는 경우
            .map(it -> ResponseEntity.ok().body(PostResponseV2.of(it)))
            // 찾는 포스트잇이 없는 경우
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/delete/{id}")
    public Mono<ResponseEntity<?>> deletePost(
        @PathVariable Long id
    ) {
        return postServiceV2.deleteById(id)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }



}
