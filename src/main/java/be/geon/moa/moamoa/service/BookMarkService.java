package be.geon.moa.moamoa.service;

import be.geon.moa.moamoa.controller.dto.BookMarkResponse;
import be.geon.moa.moamoa.domain.Bookmark;
import be.geon.moa.moamoa.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;


    @Transactional
    public Bookmark createBookmark(String url, String category) {
        Bookmark bookmark = Bookmark.createBookmark(url, category);
        return bookMarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookMarkResponse> getAllBookmarks(){
        return bookMarkRepository.getRecentBookmarks().stream()
                .map(BookMarkResponse::from)
                .toList();
    }


}
