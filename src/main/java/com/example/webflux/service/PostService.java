package com.example.webflux.service;

import com.example.webflux.client.PostClient;
import com.example.webflux.controller.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    // web client mvc server request
    private final PostClient postClient;

    public Mono<PostResponse> getPostContent(Long id) {
        return postClient.getPost(id)
            // stream 데이터 중 일부에 error가 발생해도 정상적으로 응답하기 위함.
            .onErrorResume(error -> Mono.just(new PostResponse(id.toString(), "에러 발생: %d".formatted(id))));
    }

    public Flux<PostResponse> getMultiplePostContent(List<Long> ids) {
        return Flux.fromIterable(ids)
            .flatMap(this::getPostContent)
            .log();
    }

    public Flux<PostResponse> getParallelMultiplePostContent(List<Long> ids) {
        return Flux.fromIterable(ids)
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap(this::getPostContent)
            .log()
            .sequential();
    }


}
