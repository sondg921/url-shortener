package com.example.url_shortener;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.net.URI;

@RestController // 1. "여기는 Rest API를 처리하는 곳이야" (JSON 응답)
@RequiredArgsConstructor // Service를 자동으로 데려옴 (DI)
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    // 문 1: 줄여줘! (POST 요청)
    // 요청 주소: http://localhost:8080/api/shorten?originalUrl=https://naver.com
    @PostMapping("/api/shorten")
    public String shortenUrl(@RequestParam String originalUrl){
        String shortKey = shortUrlService.generateShortUrl(originalUrl);

        // 결과로 "http://localhost:8080/aX9" 형태의 문자열을 줍니다.
        return "http://localhost:8080/" + shortKey;
    }

    // 문 2: 이동시켜줘! (GET 요청)
    // 요청 주소: http://localhost:8080/aX9
    @GetMapping("/{shortKey}")
    public ResponseEntity<?> redirectUrl(@PathVariable String shortKey){
        // 1. Service에게 원본 주소를 물어봄
        String originalUrl = shortUrlService.getOriginalUrl(shortKey);

        // 2. 브라우저에게 "이리로 이동해!"라고 명령 (HTTP 302 Redirect)
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl)); // 헤더에 목적지 주소 심기

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY); // 상태 코드 301 전송
    }
}
