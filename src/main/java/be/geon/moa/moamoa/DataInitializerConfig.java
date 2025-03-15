package be.geon.moa.moamoa;

import be.geon.moa.moamoa.domain.Bookmark;
import be.geon.moa.moamoa.domain.BookmarkCategory;
import be.geon.moa.moamoa.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {

    private final BookMarkRepository bookmarkRepository;

    /**
     * 개발 프로필에서만 실행되는 데이터 초기화 CommandLineRunner
     * @return CommandLineRunner 구현체
     */
    @Bean
    @Profile("dev") // 개발 환경에서만 실행
    public CommandLineRunner initializeTestData() {
        return args -> {
            log.info("테스트 데이터 초기화 시작...");

            // 기존 데이터가 있는지 확인
            long count = bookmarkRepository.count();

            if (count > 0) {
                log.info("이미 {}개의 북마크 데이터가 존재합니다. 초기화를 건너뜁니다.", count);
                return;
            }

            // 100개의 테스트 북마크 생성
            List<Bookmark> bookmarks = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                Bookmark bookmark = Bookmark.createBookmark("https://example.com/page" + i, BookmarkCategory.ARTICLE.getDisplayName());
                bookmarks.add(bookmark);
            }

            // 벌크 저장
            bookmarkRepository.saveAll(bookmarks);

            log.info("{}개의 테스트 북마크 데이터가 성공적으로 생성되었습니다.", bookmarks.size());
        };
    }
}