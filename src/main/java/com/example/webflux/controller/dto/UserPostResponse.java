package com.example.webflux.controller.dto;

import com.example.webflux.model.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserPostResponse {

    private Long id;

    private String userName;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserPostResponse of(Post post) {

        return UserPostResponse.builder()
            .id(post.getId())
            .userName(post.getUser().getName())
            .title(post.getTitle())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }
}
