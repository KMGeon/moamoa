package be.geon.moa.moamoa.repository;

import be.geon.moa.moamoa.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b order by b.createdAt desc")
    List<Bookmark> getRecentBookmarks();
}
