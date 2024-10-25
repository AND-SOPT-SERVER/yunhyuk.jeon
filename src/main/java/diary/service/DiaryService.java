package diary.service;

import diary.dto.Diary;
import diary.exception.RateLimitException;
import diary.repository.DiaryEntity;
import diary.repository.DiaryEntity.Category;
import diary.repository.DiaryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(String title, String content, Category category) {
        if (diaryRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("제목은 중복된 값이 불가능합니다.");
        }

        if (title.length() > 10) {
            throw new IllegalArgumentException("제목은 10자 이하로 작성해주세요.");
        } else if (content.length() > 30) {
            throw new IllegalArgumentException("내용은 30자 이하로 작성해주세요.");
        }

        DiaryEntity lastDiary = diaryRepository.findTopByOrderByIdDesc().orElse(null);
        if (lastDiary != null) {
            LocalDateTime lastDate = lastDiary.getDate();
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(lastDate.plusMinutes(5))) {
                throw new RateLimitException("5분에 한 번 일기를 작성할 수 있습니다.");
            }
        }

        diaryRepository.save(new DiaryEntity(title, content, LocalDateTime.now(), category));
    }

    public Flux<Diary> getAllDiary() {
        return Flux.fromIterable(diaryRepository.findAllByOrderByIdDesc())
                .map(diaryEntity -> new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory()));
    }

    public Flux<Diary> getRecentDiary() {
        return Flux.fromIterable(diaryRepository.findTop10ByOrderByIdDesc())
                .map(diaryEntity -> new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory()));
    }

    public Flux<Diary> getSortedDiary() {
        return Flux.fromIterable(diaryRepository.findTop10ByContentLength(PageRequest.of(0, 10)))
                .map(diaryEntity -> new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory()));
    }

    public Flux<Diary> getDiariesByCategory(Category category) {
        return Flux.fromIterable(diaryRepository.findByCategoryOrderByIdDesc(category))
                .map(diaryEntity -> new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory()));
    }

    public Diary getDiaryById(Long id) {
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일기를 찾을 수 없습니다. ID: " + id));

        return new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory());
    }

    public void updateDiary(Long id, String title, String content, Category category) {
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일기를 찾을 수 없습니다. ID: " + id));

        if (title.length() > 10) {
            throw new IllegalArgumentException("제목은 10자 이하로 작성해주세요.");
        } else if (content.length() > 30) {
            throw new IllegalArgumentException("내용은 30자 이하로 작성해주세요.");
        }

        diaryEntity.setTitle(title);
        diaryEntity.setContent(content);
        diaryEntity.setCategory(category);
        diaryRepository.save(diaryEntity);
    }

    public void deleteDiary(Long id) {
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일기를 찾을 수 없습니다. ID: " + id));
        diaryRepository.deleteById(id);
    }
}
