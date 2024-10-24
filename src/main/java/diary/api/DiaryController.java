package diary.api;

import diary.dto.DiaryDetailResponse;
import diary.dto.DiaryListResponse;
import diary.dto.DiaryRequest;
import diary.dto.DiaryResponse;
import diary.dto.Diary;
import diary.repository.DiaryEntity;
import diary.service.DiaryService;
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
    void post(@RequestBody DiaryRequest diaryRequest){
        diaryRequest.validate();
        diaryService.createDiary(diaryRequest.title(), diaryRequest.content(), diaryRequest.category());
    }

    @GetMapping("/api/diary/all")
    ResponseEntity<DiaryListResponse> get(){
        List<Diary> diaryList = diaryService.getAllDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.category()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary")
    ResponseEntity<DiaryListResponse> getRecent(){
        List<Diary> diaryList = diaryService.getRecentDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.category()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/sorted")
    ResponseEntity<DiaryListResponse> getSorted(){
        List<Diary> diaryList = diaryService.getSortedDiary();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.category()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/{id}")
    ResponseEntity<DiaryDetailResponse> getById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        return ResponseEntity.ok(new DiaryDetailResponse(diary.id(), diary.title(), diary.content(), diary.date(), diary.category()));
    }

    @GetMapping("/api/diary/category/{category}")
    ResponseEntity<DiaryListResponse> getCategory(@PathVariable DiaryEntity.Category category){
        List<Diary> diaryList = diaryService.getDiariesByCategory(category);

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for (Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.id(), diary.title(), diary.category()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @PatchMapping("/api/diary/{id}")
    void update(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest){
        diaryRequest.validate();
        diaryService.updateDiary(id, diaryRequest.title(), diaryRequest.content(), diaryRequest.category());
    }

    @DeleteMapping("/api/diary/{id}")
    void delete(@PathVariable Long id){
        diaryService.deleteDiary(id);
    }
}
