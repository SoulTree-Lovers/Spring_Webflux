package com.example.webflux.controller;

import com.example.webflux.controller.dto.UserRequest;
import com.example.webflux.controller.dto.UserResponse;
import com.example.webflux.model.User;
import com.example.webflux.service.PostServiceV2;
import com.example.webflux.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@AutoConfigureWebTestClient
class UserControllerTest {

    static {
        BlockHound.install(
            builder -> builder.allowBlockingCallsInside("클래스 이름", "메소드 이름"), // 특정 클래스의 메소드에서 발생하는 블라킹은 허용
        );
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService; // 가짜 user service를 활용

    @MockBean
    private PostServiceV2 postServiceV2;

    /**
     * 테스트 코드 내에서 블라킹 발생 시 예외 발생
     * (코드 상 반드시 필요한 블라킹이 필요한 부분도 잡게 됨.)
     * (이 부분은 따로 제거할 수 있음.)
     */
    @Test
    void blockHoundTest() {
        StepVerifier.create(Mono.delay(Duration.ofSeconds(1))
                .doOnNext(it -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }))
            .verifyComplete();
    }

    @Test
    void createUser() {

        when(userService.create("ex_name", "ex_email")).thenReturn(
            Mono.just(new User(1L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.post().uri("/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserRequest("ex_name", "ex_email"))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(UserResponse.class)
            .value(res -> {
                assertEquals("ex_name", res.getName());
                assertEquals("ex_email", res.getEmail());
            });
    }

    @Test
    void findAllUsers() {

        when(userService.findAll()).thenReturn(
            Flux.just(
                new User(1L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now()),
                new User(2L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now()),
                new User(3L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now())
            )
        );

        webTestClient.get().uri("/users/find/all")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(UserResponse.class)
            .hasSize(3);

    }

    @Test
    void findUser() {

        when(userService.findById(1L)).thenReturn(
            Mono.just(new User(1L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.get().uri("/users/find/1")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(UserResponse.class)
            .value( res -> {
                assertEquals("ex_name", res.getName());
                assertEquals("ex_email", res.getEmail());
            });
    }

    @Test
    void notFoundUser() {

        when(userService.findById(1L)).thenReturn(
            Mono.empty()
        );

        webTestClient.get().uri("/users/find/1")
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void updateUser() {
        when(userService.update(1L, "ex_name", "ex_email")).thenReturn(
            Mono.just(new User(1L, "ex_name", "ex_email", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.put().uri("/users/update/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserRequest("ex_name", "ex_email"))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(UserResponse.class)
            .value(res -> {
                assertEquals("ex_name", res.getName());
                assertEquals("ex_email", res.getEmail());
            });
    }

    @Test
    void deleteUser() {

        when(userService.deleteById(1L)).thenReturn(
//            Mono.just(1)
            Mono.empty()
        );

        webTestClient.delete().uri("/users/delete/1")
            .exchange()
            .expectStatus().is2xxSuccessful();
    }
}