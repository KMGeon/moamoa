package be.geon.moa.moamoa.repository;

import be.geon.moa.moamoa.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
}
