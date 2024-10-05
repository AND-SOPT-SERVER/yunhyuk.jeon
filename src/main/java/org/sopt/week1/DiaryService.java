package org.sopt.week1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiaryService {
    private final DiaryRepository diaryRepository = new DiaryRepository();

    public List<String> getDiary() {
        return new ArrayList<>(diaryRepository.findAll().values());
    }

    public Long createDiary(String content) {
        return diaryRepository.save(content);
    }

    public boolean deleteDiary(Long id) {
        return diaryRepository.deleteById(id);
    }

    public boolean updateDiary(Long id, String newContent) {
        return diaryRepository.update(id, newContent);
    }
}
