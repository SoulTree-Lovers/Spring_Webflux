package com.example.webflux.controller;

import com.example.webflux.controller.dto.UserRequest;
import com.example.webflux.controller.dto.UserResponse;
import com.example.webflux.controller.dto.UserUpdateRequest;
import com.example.webflux.model.User;
import com.example.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public Mono<UserResponse> createUser(
        @RequestBody UserRequest userRequest
    ) {
        return userService.create(userRequest.getName(), userRequest.getEmail())
            .map(UserResponse::of);
    }

    @GetMapping("/find/all")
    public Flux<UserResponse> findAllUsers() {
        return userService.findAll()
            .map(UserResponse::of);
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<UserResponse>> findUser(
        @PathVariable Long id
    ) {
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(UserResponse.of((User) user))) // user가 있으면 200 ok
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())); // user가 없으면 404 not found
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(
        @PathVariable Long id,
        @RequestBody UserUpdateRequest userUpdateRequest
        ) {
        return userService.update(id, userUpdateRequest.getName(), userUpdateRequest.getEmail())
            .map(user -> ResponseEntity.ok(UserResponse.of((User) user))) // user가 있으면 200 ok
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())); // user가 없으면 404 not found
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<?>> deleteUser(
        @PathVariable Long id
    ) {
        // 204로 리턴 (no content)
        return userService.deleteById(id)
            .then(
                Mono.just(ResponseEntity.noContent().build()) // 204 리턴
            );
    }


}
