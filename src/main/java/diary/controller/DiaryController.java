package diary.controller;

import diary.dto.*;
import diary.repository.DiaryEntity;
import diary.service.DiaryService;
import diary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    @PostMapping("/api/diary")
    void post(@RequestBody DiaryRequest diaryRequest, @RequestHeader("Authorization") Long token){
        diaryRequest.validate();
        diaryService.createDiary(token, diaryRequest.title(), diaryRequest.content(), diaryRequest.category(), diaryRequest.isVisible());
    }

    @GetMapping("/api/diary/all")
    ResponseEntity<DiaryListResponse> get(@RequestHeader("Authorization") Long token){
        List<Diary> diaryList = diaryService.getAllDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title() ,diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary")
    ResponseEntity<DiaryListResponse> getRecent(@RequestHeader("Authorization") Long token){
        List<Diary> diaryList = diaryService.getRecentDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/sorted")
    ResponseEntity<DiaryListResponse> getSorted(@RequestHeader("Authorization") Long token){
        List<Diary> diaryList = diaryService.getSortedDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/{id}")
    ResponseEntity<DiaryDetailResponse> getById(@PathVariable Long id, @RequestHeader("Authorization") Long token) {
        Diary diary = diaryService.getDiaryById(id);

        return ResponseEntity.ok(new DiaryDetailResponse(diary.id(), diary.title(), diary.content(), diary.date(), diary.category(), diary.isVisible()));
    }

    @GetMapping("/api/diary/category/{category}")
    ResponseEntity<DiaryListResponse> getByCategory(@PathVariable DiaryEntity.Category category, @RequestHeader("Authorization") Long token){
        List<Diary> diaryList = diaryService.getDiariesByCategory(category);

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/user")
    ResponseEntity<DiaryListResponse> getByUserId(@RequestHeader("Authorization") Long token){
        List<Diary> diaryList = diaryService.getUserDiary(token);

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title() ,diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @PatchMapping("/api/diary/{id}")
    void update(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest, @RequestHeader("Authorization") Long token){
        diaryRequest.validate();
        diaryService.updateDiary(id, diaryRequest.title(), diaryRequest.content(), diaryRequest.category());
    }

    @DeleteMapping("/api/diary/{id}")
    void delete(@PathVariable Long id, @RequestHeader("Authorization") Long token){
        diaryService.deleteDiary(id);
    }
}
