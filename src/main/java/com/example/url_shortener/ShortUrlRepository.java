package com.example.url_shortener;

import org.springframework.data.jpa.repository.JpaRepository;

// <다룰 엔티티 클래스, 그 엔티티의 PK 타입>
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // "select * from short_url where shortKey = ?" 쿼리를 대신해주는 메서드
    // 이름만 규칙에 맞게 지으면 구현은 스프링이 알아서 해줍니다.
    ShortUrl findByShortKey(String shortKey);
}