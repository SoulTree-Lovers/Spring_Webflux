package com.example.webflux.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class R2dbcConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final DatabaseClient databaseClient;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        databaseClient.sql("SELECT 1").fetch().one()
            .subscribe(
                success -> {
                    log.info("Initialize r2dbc database connection.");
                },
                error -> {
                    log.error("Failed to initialize r2dbc database connection.");
                }
            );

    }
}
