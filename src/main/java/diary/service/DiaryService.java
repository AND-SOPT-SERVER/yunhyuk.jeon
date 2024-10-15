package diary.service;

import diary.repository.DiaryEntity;
import diary.repository.DiaryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(){
        diaryRepository.save(new DiaryEntity("윤혁"));
    }

    public ArrayList<Diary> getList (){
        final List<DiaryEntity> diaryEntityList = diaryRepository.findAll();

        final ArrayList<Diary> diaryList = new ArrayList<>();

        for (DiaryEntity diaryEntity : diaryEntityList){
            diaryList.add(
                    new Diary(diaryEntity.getId(),diaryEntity.getName())
            );
        }

        return diaryList;
    }
}
