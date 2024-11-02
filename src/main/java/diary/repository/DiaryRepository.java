package diary.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    Optional<DiaryEntity> findTopByIsVisibleTrueOrderByIdDesc();
    List<DiaryEntity> findAllByIsVisibleTrueOrderByIdDesc();
    List<DiaryEntity> findTop10ByIsVisibleTrueOrderByIdDesc();
    @Query("SELECT d FROM DiaryEntity d WHERE d.isVisible = true ORDER BY LENGTH(d.content) DESC, d.id DESC")
    List<DiaryEntity> findTop10ByIsVisibleContentLength(PageRequest pageRequest);
    boolean existsByTitle(String title);
    List<DiaryEntity> findByCategoryAndIsVisibleTrueOrderByIdDesc(DiaryEntity.Category category);
    List<DiaryEntity> findByUserIdOrderByIdDesc(Long userId);
}
