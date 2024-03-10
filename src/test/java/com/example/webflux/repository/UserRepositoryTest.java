package com.example.webflux.repository;

import com.example.webflux.model.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Test
    void save() {
        User user = User.builder()
            .name("ex_name")
            .email("ex_email")
            .build();

        StepVerifier.create(userRepository.save(user))
            .assertNext( it -> {
                assertEquals(1L, it.getId());
                assertEquals("ex_name", it.getName());
                assertEquals("ex_email", it.getEmail());
            })
            .verifyComplete();
    }

    @Test
    void findAll() {

        userRepository.save(User.builder().name("n1").email("e1").build());
        userRepository.save(User.builder().name("n2").email("e2").build());
        userRepository.save(User.builder().name("n3").email("e3").build());

        StepVerifier.create(userRepository.findAll())
            .expectNextCount(3)
            .verifyComplete();

    }

    @Test
    void findById() {

        userRepository.save(User.builder().name("n1").email("e1").build());

        StepVerifier.create(userRepository.findById(1L))
            .assertNext(it -> {
                assertEquals(1L, it.getId());
                assertEquals("n1", it.getName());
                assertEquals("e1", it.getEmail());
            })
            .verifyComplete();

    }

    @Test
    void deleteById() {

        userRepository.save(User.builder().name("n1").email("e1").build());

        StepVerifier.create(userRepository.deleteById(1L))
            .expectNext(1)
            .verifyComplete();

    }
}