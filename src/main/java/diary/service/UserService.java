package diary.service;

import diary.dto.Diary;
import diary.exception.RateLimitException;
import diary.repository.DiaryEntity;
import diary.repository.DiaryEntity.Category;
import diary.repository.DiaryRepository;
import diary.repository.UserEntity;
import diary.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createDiary(Long token, String title, String content, Category category, boolean isVisible) {
        if (diaryRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("제목은 중복된 값이 불가능합니다.");
        }

        if (title.length() > 10) {
            throw new IllegalArgumentException("제목은 10자 이하로 작성해주세요.");
        } else if (content.length() > 30) {
            throw new IllegalArgumentException("내용은 30자 이하로 작성해주세요.");
        }

        DiaryEntity lastDiary = diaryRepository.findTopByIsVisibleTrueOrderByIdDesc().orElse(null);
        if (lastDiary != null) {
            LocalDateTime lastDate = lastDiary.getDate();
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(lastDate.plusMinutes(5))) {
                throw new RateLimitException("5분에 한 번 일기를 작성할 수 있습니다.");
            }
        }

        UserEntity userEntity = userRepository.findById(token)
                .orElseThrow(() -> new NoSuchElementException("사용자 식별자를 찾을 수 없습니다. token: " + token));

        diaryRepository.save(new DiaryEntity(userEntity, title, content, LocalDateTime.now(), category, isVisible));
    }

    public ArrayList<Diary> getAllDiary() {
        final List<DiaryEntity> diaryEntityList = diaryRepository.findAllByIsVisibleTrueOrderByIdDesc();
        final ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible())
            );
        }

        return diaryList;
    }

    public ArrayList<Diary> getRecentDiary() {
        List<DiaryEntity> diaryEntityList = diaryRepository.findTop10ByIsVisibleTrueOrderByIdDesc();
        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible())
            );
        }

        return diaryList;
    }

    public ArrayList<Diary> getSortedDiary() {
        List<DiaryEntity> diaryEntityList = diaryRepository.findTop10ByIsVisibleContentLength(PageRequest.of(0, 10));
        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible())
            );
        }

        return diaryList;
    }

    public ArrayList<Diary> getUserDiary(Long userId){
        List<DiaryEntity> diaryEntityList = diaryRepository.findByUserIdOrderByIdDesc(userId);
        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible())
            );
        }

        return diaryList;
    }

    public Diary getDiaryById(Long id) {
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일기를 찾을 수 없습니다. ID: " + id));

        return new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible());
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

    public ArrayList<Diary> getDiariesByCategory(Category category) {
        List<DiaryEntity> diaryEntityList = diaryRepository.findByCategoryAndIsVisibleTrueOrderByIdDesc(category);
        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate(), diaryEntity.getCategory(), diaryEntity.getIsVisible())
            );
        }

        return diaryList;
    }
}
