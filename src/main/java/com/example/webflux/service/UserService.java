package com.example.webflux.service;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {

//    private final UserRepository userRepository;
    private final UserR2dbcRepository userR2dbcRepository;

    // Redis에서 User(사용자 정의 클래스)를 사용하려면 설정을 따로 해줘야 함
    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;

    // CRUD

    public Mono<User> create(String name, String email) {
        return userR2dbcRepository.save(
            User.builder()
                .name(name)
                .email(email)
                .build()
        );
    }

    public Flux<User> findAll() {
        return userR2dbcRepository.findAll();
    }

    private String getUserCacheKey(Long id) {
        return "users:%d".formatted(id);
    }

    public Mono<User> findById(Long id) {
        return reactiveRedisTemplate.opsForValue() // 1. redis 조회
            .get(getUserCacheKey(id)) // 2. Redis에 값이 존재하면 응답을 하고
            .switchIfEmpty( // 3. 없으면
                userR2dbcRepository.findById(id) // 4. DB에 질의하고
                    .flatMap(user -> reactiveRedisTemplate.opsForValue() // 5. 결과를 Redis에 저장
                        .set(getUserCacheKey(id), user, Duration.ofSeconds(30)) // 6. 저장된 데이터는 30초간만 유지
                        .then(Mono.just(user))
                    )
            );

//        return userR2dbcRepository.findById(id);
    }

//    public Mono<Integer> deleteById(Long id) {
//        return userR2dbcRepository.deleteById(id);
//    }

    public Mono<Void> deleteById(Long id) {
        return userR2dbcRepository.deleteById(id)
            .then(reactiveRedisTemplate.unlink(getUserCacheKey(id)))
            .then(Mono.empty());
    }

    public Mono<Void> deleteByName(String name) {
        return userR2dbcRepository.deleteByName(name);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userR2dbcRepository.findById(id)
            .flatMap(user -> {
                user.setName(name);
                user.setEmail(email);
                return userR2dbcRepository.save(user);
            })
            .flatMap(user -> reactiveRedisTemplate.unlink(getUserCacheKey(id)) // 업데이트 후 레디스에서 기존 데이터 삭제 unlink(비동기 삭제), delete(동기 삭제)
                .then(Mono.just(user))
            )
            ;
    }
}
