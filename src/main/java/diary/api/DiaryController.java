package diary.api;

import diary.dto.DiaryDetailResponse;
import diary.dto.DiaryListResponse;
import diary.dto.DiaryRequest;
import diary.dto.DiaryResponse;
import diary.service.Diary;
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
        diaryService.createDiary(diaryRequest.title(), diaryRequest.content());
    }

    @GetMapping("/api/diary")
    ResponseEntity<DiaryListResponse> get(){
        List<Diary> diaryList = diaryService.getList();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getTitle()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/recent")
    ResponseEntity<DiaryListResponse> getRecent(){
        List<Diary> diaryList = diaryService.getRecentList();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getTitle()));
        }

        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }

    @GetMapping("/api/diary/{id}")
    ResponseEntity<DiaryDetailResponse> getById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);

        return ResponseEntity.ok(new DiaryDetailResponse(diary.getId(), diary.getTitle(), diary.getContent(), diary.getDate()));
    }

    @PatchMapping("api/diary/{id}")
    void update(@PathVariable Long id, @RequestBody DiaryRequest diaryRequest){
        diaryService.updateDiary(id, diaryRequest.title(), diaryRequest.content());
    }

    @DeleteMapping("api/diary/{id}")
    void delete(@PathVariable Long id){
        diaryService.deleteDiary(id);
    }
}
