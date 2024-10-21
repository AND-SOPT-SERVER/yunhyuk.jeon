package diary.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    Optional<DiaryEntity> findTopByOrderByIdDesc();
    List<DiaryEntity> findAllByOrderByIdDesc();
    List<DiaryEntity> findTop10ByOrderByIdDesc();
    @Query("SELECT d FROM DiaryEntity d ORDER BY LENGTH(d.content) DESC, d.id DESC")
    List<DiaryEntity> findTop10ByContentLength(PageRequest pageRequest);
    DiaryEntity findByTitle(String title);
}
