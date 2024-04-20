package com.example.webflux.repository;

import com.example.webflux.model.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC DB
 */
public interface UserR2dbcRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByName(String name);

    Flux<User> findByNameOrderByIdDesc(String name);

    // Query를 직접 명시하는 예시
    @Modifying // 데이터를 직접 변경할 경우 붙여줘야 함.
    @Query("DELETE FROM users WHERE name = :name")
    Mono<Void> deleteByName(String name);
}
