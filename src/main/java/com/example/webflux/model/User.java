package com.example.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") // table 연동
public class User {

    @Id
    private Long id;

    private String name;

    private String email;

    @CreatedDate // 생성 시간 자동 기록
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시간 자동 기록
    private LocalDateTime updatedAt;

}
