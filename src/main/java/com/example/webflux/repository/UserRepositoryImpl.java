package com.example.webflux.repository;

import com.example.webflux.model.User;
import com.example.webflux.repository.UserRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(1L); // 1부터 시작하는 원자적 id

    @Override
    public Mono<User> save(User user) {
        LocalDateTime currTime = LocalDateTime.now();

        if (user.getId() == null) {
            user.setId(sequence.getAndIncrement());
            user.setCreatedAt(currTime);
        }

        user.setUpdatedAt(currTime);

        users.put(user.getId(), user);

        return Mono.just(user);
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(users.values());
    }

    @Override
    public Mono<User> findById(Long id) {
        return Mono.justOrEmpty(users.getOrDefault(id, null));
    }

    @Override
    public Mono<Integer> deleteById(Long id) {
        User user = users.getOrDefault(id, null);
        if (user == null) {
            return Mono.just(0); // 삭제할 객체가 없으면 0 리턴
        }
        users.remove(id, user);
        return Mono.just(1); // 삭제할 객체가 있으면 1 리턴
    }
}
