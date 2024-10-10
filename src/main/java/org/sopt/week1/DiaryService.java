package org.sopt.week1;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaryService {
    private final DiaryRepository diaryRepository = new DiaryRepository();

    public List<String> getDiary() {
        return diaryRepository.findAll();
    }

    public Long createDiary(String content) {
        if (countLength(content) > 30) {
            System.out.println("일기는 30자 이하로 작성해야 합니다.");
            return null;
        }
        return diaryRepository.save(content);
    }

    public boolean deleteDiary(Long id) {
        return diaryRepository.deleteById(id);
    }

    public boolean updateDiary(Long id, String newContent) {
        if (countLength(newContent) > 30) {
            System.out.println("일기는 30자 이하로 작성해야 합니다.");
            return false;
        }

        try {
            LocalDate today = LocalDate.now();
            LocalDate lastUpdateDate = diaryRepository.readUpdateDate();

            if (lastUpdateDate == null || lastUpdateDate.isBefore(today)) {
                diaryRepository.clearUpdateCount();
            } else if (lastUpdateDate.isEqual(today)) {
                long updateCount = diaryRepository.getUpdateCount();
                if (updateCount >= 2) {
                    System.out.println("일일 수정 제한을 초과했습니다.");
                    return false;
                }
            }
            return diaryRepository.update(id, newContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean restoreDiary(Long id) {
        return diaryRepository.restore(id);
    }

    private static final Pattern graphemePattern = Pattern.compile("\\X");
    private static final Matcher graphemeMatcher = graphemePattern.matcher("");

    public static int countLength(String text) {
        if (text == null) {
            return 0;
        }
        graphemeMatcher.reset(text);
        int count = 0;
        while (graphemeMatcher.find()) {
            count++;
        }
        return count;
    }
}
