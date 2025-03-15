package be.geon.moa.moamoa.controller.dto;

import be.geon.moa.moamoa.domain.Bookmark;
import lombok.Builder;

import java.time.LocalDateTime;

import static be.geon.moa.moamoa.utils.Utils.formatLocalDateTime;

@Builder
public record BookMarkResponse (
        String bookmarkUid,
        String url,
        String category,
        String createdAt
){
    public static BookMarkResponse from(Bookmark bookmark) {
        return BookMarkResponse.builder()
                .bookmarkUid(bookmark.getBookmarkUid().toString())
                .url(bookmark.getUrl())
                .category(bookmark.getCategory().name())
                .createdAt(formatLocalDateTime(bookmark.getCreatedAt()))
                .build();
    }
}
