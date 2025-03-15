package be.geon.moa.moamoa.service;

import be.geon.moa.moamoa.domain.Bookmark;
import be.geon.moa.moamoa.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public Bookmark save(Bookmark bookmark) {
        return bookMarkRepository.save(bookmark);
    }

}
