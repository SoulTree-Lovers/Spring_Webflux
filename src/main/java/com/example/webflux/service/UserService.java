package com.example.webflux.service;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;
import com.example.webflux.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // CRUD

    public Mono<User> create(String name, String email) {
        return userRepository.save(
            User.builder()
                .name(name)
                .email(email)
                .build()
        );
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<Integer> deleteById(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userRepository.findById(id)
            .flatMap(user -> {
                user.setName(name);
                user.setEmail(email);
                return userRepository.save(user);
            });
    }
}
