package diary.service;

import diary.dto.Diary;
import diary.exception.ConflictException;
import diary.exception.LengthException;
import diary.exception.NotFoundException;
import diary.exception.TimeLimitException;
import diary.repository.DiaryEntity;
import diary.repository.DiaryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(String title, String content){
        if (title.length() > 10) {
            throw new LengthException("제목은 10자 이하로 작성해주세요.");
        } else if (content.length() > 30) {
            throw new LengthException("내용은 30자 이하로 작성해주세요.");
        }

        if (diaryRepository.findByTitle(title) != null) {
            throw new ConflictException("같은 제목의 일기가 이미 존재합니다.");
        }

        DiaryEntity lastDiary = diaryRepository.findTopByOrderByIdDesc().orElse(null);
        if (lastDiary != null) {
            LocalDateTime lastDate = lastDiary.getDate();
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(lastDate.plusMinutes(5))) {
                throw new TimeLimitException("5분에 한 번 일기를 작성할 수 있습니다.");
            }
        }

        diaryRepository.save(new DiaryEntity(title, content, LocalDateTime.now()));
    }

    public ArrayList<Diary> getAllDiary(){
        final List<DiaryEntity> diaryEntityList = diaryRepository.findAllByOrderByIdDesc();

        final ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList){
            diaryList.add(
                    new Diary(diaryEntity.getId(),diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate())
            );
        }

        return diaryList;
    }

    public ArrayList<Diary> getRecentDiary() {
        List<DiaryEntity> diaryEntityList = diaryRepository.findTop10ByOrderByIdDesc();

        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate())
            );
        }

        return diaryList;
    }

    public ArrayList<Diary> getSortedDiary() {
        List<DiaryEntity> diaryEntityList = diaryRepository.findTop10ByContentLength(PageRequest.of(0, 10));

        ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList) {
            diaryList.add(
                    new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate())
            );
        }

        return diaryList;
    }

    public Diary getDiaryById(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("일기를 찾을 수 없습니다. ID: " + id));

        return new Diary(diaryEntity.getId(), diaryEntity.getTitle(), diaryEntity.getContent(), diaryEntity.getDate());
    }

    public void updateDiary(Long id, String title, String content){
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("일기를 찾을 수 없습니다. ID: " + id));

        if (title.length() > 10) {
            throw new LengthException("제목은 10자 이하로 작성해주세요.");
        } else if (content.length() > 30) {
            throw new LengthException("내용은 30자 이하로 작성해주세요.");
        }

        diaryEntity.setTitle(title);
        diaryEntity.setContent(content);
        diaryRepository.save(diaryEntity);
    }

    public void deleteDiary(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("일기를 찾을 수 없습니다. ID: " + id));
        diaryRepository.deleteById(id);
    }
}
