package be.geon.moa.moamoa.domain;

import be.geon.moa.moamoa.common.InvalidURLException;
import be.geon.moa.moamoa.common.NotFoundCategoryException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.UUID;

@Getter
@Entity
@Table(
        name = "bookmark",
        indexes = {
                @Index(name = "idx_created_desc", columnList = "created_at DESC")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bookmark_uid", nullable = false, unique = true, updatable = false)
    private UUID bookmarkUid;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookmarkCategory category;

    @Builder
    private Bookmark(String url, BookmarkCategory category) {
        Assert.hasText(url, "URL은 필수입니다");
        Assert.notNull(category, "카테고리는 필수입니다");

        this.bookmarkUid = UUID.randomUUID();
        this.url = url;
        this.category = category;
    }

    public static Bookmark createBookmark(String url, String category) {
        if (!isValidUrl(url)) throw new InvalidURLException("유효하지 않은 URL 형식입니다: " + url);

        BookmarkCategory bookmarkCategory = BookmarkCategory.fromDisplayName(category);
        if (bookmarkCategory == null) throw new NotFoundCategoryException(String.format("유효하지 않은 카테고리입니다: %s", category));

        return Bookmark.builder()
                .url(url)
                .category(bookmarkCategory)
                .build();
    }

    private static boolean isValidUrl(String url) {
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void changeUrl(String newUrl) {
        Assert.hasText(newUrl, "URL은 필수입니다");
        this.url = newUrl;
    }

    public void changeCategory(BookmarkCategory newCategory) {
        Assert.notNull(newCategory, "카테고리는 필수입니다");
        this.category = newCategory;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "id=" + id +
                ", bookmarkUid=" + bookmarkUid +
                ", url='" + url + '\'' +
                ", category=" + category +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return bookmarkUid.equals(bookmark.bookmarkUid);
    }

    @Override
    public int hashCode() {
        return bookmarkUid.hashCode();
    }
}