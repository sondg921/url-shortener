package com.example.url_shortener;

import jakarta.persistence.*;
import lombok.Data;

@Data // 1. Lombok: Getter, Setter 자동 생성
@Entity // 2. JPA: 이 클래스가 곧 DB 테이블이다
public class ShortUrl {
    @Id // 3. PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto Increment
    private Long id;

    @Column(nullable = false) // null 불가능
    private String originalUrl;

    private String shortKey;
}
