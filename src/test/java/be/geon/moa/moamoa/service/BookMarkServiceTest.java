package be.geon.moa.moamoa.service;

import be.geon.moa.moamoa.common.InvalidURLException;
import be.geon.moa.moamoa.common.NotFoundCategoryException;
import be.geon.moa.moamoa.domain.Bookmark;
import be.geon.moa.moamoa.domain.BookmarkCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookMarkServiceTest {

    @Autowired
    private BookMarkService bookMarkService;

    private static final String CATEGORY = BookmarkCategory.BLOG.getDisplayName();


    @Test
    public void 북마크_생성() throws Exception{
        // given
        String url = "http://www.google.com";
        // when
        Bookmark rtn = bookMarkService.createBookmark(url, CATEGORY);

        // then
        assertAll(
                () -> Assertions.assertThat(rtn.getUrl()).isEqualTo(url),
                () -> Assertions.assertThat(rtn.getCategory()).isNotEqualTo(BookmarkCategory.BLOG.getDisplayName())
        );
    }

    @Test
    public void 북마크_생성_카테고리_미매핑() throws Exception{
        // given
        String url = "http://www.google.com";
        String category = "오류";
        // when
        // then
        Assertions.assertThatThrownBy(() -> bookMarkService.createBookmark(url, category))
                        .isInstanceOf(NotFoundCategoryException.class)
                .hasMessageContaining("유효하지 않은 카테고리입니다: 오류");
    }

    @Test
    public void 북마크_생성_URL_유효성() throws Exception{
        // given
        String url = "error.google.com";
        // when
        // then
        Assertions.assertThatThrownBy(() -> bookMarkService.createBookmark(url, CATEGORY))
                .isInstanceOf(InvalidURLException.class)
                .hasMessageContaining("유효하지 않은 URL 형식입니다: error.google.com");
    }


}