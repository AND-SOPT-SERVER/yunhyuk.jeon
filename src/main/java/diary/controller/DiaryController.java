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
    private final UserService userService;

    public DiaryController(DiaryService diaryService, UserService userService){
        this.diaryService = diaryService;
        this.userService = userService;
    }

    @PostMapping("/api/diary")
    void post(@RequestBody DiaryRequest diaryRequest, @RequestHeader("Authorization") String token){
        diaryRequest.validate();
        diaryService.createDiary(token, diaryRequest.title(), diaryRequest.content(), diaryRequest.category(), diaryRequest.isVisible());
    }

    @GetMapping("/api/diary/all")
    ResponseEntity<DiaryListResponse> get(@RequestHeader("Authorization") String token){
        List<Diary> diaryList = diaryService.getAllDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), token ,diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary")
    ResponseEntity<DiaryListResponse> getRecent(@RequestHeader("Authorization") String token){
        List<Diary> diaryList = diaryService.getRecentDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), token, diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/sorted")
    ResponseEntity<DiaryListResponse> getSorted(@RequestHeader("Authorization") String token){
        List<Diary> diaryList = diaryService.getSortedDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), token, diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/{id}")
    ResponseEntity<DiaryDetailResponse> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Diary diary = diaryService.getDiaryById(id);

        return ResponseEntity.ok(new DiaryDetailResponse(diary.id(), diary.title(), diary.content(), token, diary.date(), diary.category(), diary.isVisible()));
    }

    @GetMapping("/api/diary/category/{category}")
    ResponseEntity<DiaryListResponse> getCategory(@PathVariable DiaryEntity.Category category, @RequestHeader("Authorization") String token){
        List<Diary> diaryList = diaryService.getDiariesByCategory(category);

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), token, diary.date(), diary.category(), diary.isVisible()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @PatchMapping("/api/diary/{id}")
    void update(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest, @RequestHeader("Authorization") String token){
        diaryRequest.validate();
        diaryService.updateDiary(id, diaryRequest.title(), diaryRequest.content(), diaryRequest.category());
    }

    @DeleteMapping("/api/diary/{id}")
    void delete(@PathVariable Long id, @RequestHeader("Authorization") String token){
        diaryService.deleteDiary(id);
    }
}
