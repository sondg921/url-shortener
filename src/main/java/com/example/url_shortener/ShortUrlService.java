package com.example.url_shortener;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // 1. 스프링에게 "이 클래스는 비즈니스 로직을 담당해"라고 알려줌 (Bean 등록)
@RequiredArgsConstructor // 2. Lombok: final이 붙은 필드의 생성자를 자동 생성 (DI 주입)
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 기능 1: URL 단축하기
    @Transactional // 3. DB 트랜잭션 보장 (중간에 실패하면 롤백)
    public String generateShortUrl(String originalUrl){
        // A. 일단 DB에 저장해서 PK(ID)를 얻음
        ShortUrl entity = new ShortUrl();
        entity.setOriginalUrl(originalUrl);
        ShortUrl savedEntity = shortUrlRepository.save(entity); // id가 생성됨 (예: 1000)

        // B. 얻은 ID를 Base62로 인코딩 (1000 -> "g8")
        String shortKey = encodeBase62(savedEntity.getId());

        // C. 단축 키를 다시 DB에 업데이트
        savedEntity.setShortKey(shortKey);

        return shortKey;
    }

    // 기능 2: 단축 키로 원본 URL 찾기
    @Transactional(readOnly = true)
    public String getOriginalUrl(String shortKey){
        ShortUrl entity = shortUrlRepository.findByShortKey(shortKey);
        if (entity == null) {
            throw new RuntimeException("존재하지 않는 단축 URL입니다.");
        }
        return entity.getOriginalUrl(); // 이때 getOriginalUrl 함수는 재귀호출이 아닌 entity의 Getter
    }

    // [알고리즘] 10진수 ID -> 62진수 문자열 변환
    private String encodeBase62(long id){
        StringBuilder sb = new StringBuilder();
        while(id > 0){
            int remainder = (int) (id %62);
            sb.append(BASE62.charAt(remainder));
            id = id / 62;
        }
        return sb.toString(); // ex: "aZ1"
    }
}