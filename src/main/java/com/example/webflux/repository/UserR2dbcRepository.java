package com.example.webflux.repository;

import com.example.webflux.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * R2DBC DB
 */
public interface UserR2dbcRepository extends ReactiveCrudRepository<User, Long> {


}
