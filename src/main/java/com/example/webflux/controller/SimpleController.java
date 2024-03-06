package com.example.webflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class SimpleController {

    @GetMapping("/get-hello")
    public Mono<String> getHello() {

        // reactor

        // publisher <--> subscriber

        return Mono.just("hello java webflux !!!");
    }
}
