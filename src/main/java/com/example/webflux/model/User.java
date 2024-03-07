package com.example.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
