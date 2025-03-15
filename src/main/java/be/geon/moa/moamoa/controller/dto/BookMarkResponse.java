package be.geon.moa.moamoa.controller.dto;

import be.geon.moa.moamoa.domain.Bookmark;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookMarkResponse (
        String bookmarkUid,
        String url,
        LocalDateTime createdAt
){
    public static BookMarkResponse from(Bookmark bookmark) {
        return BookMarkResponse.builder()
                .bookmarkUid(bookmark.getBookmarkUid().toString())
                .url(bookmark.getUrl())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
