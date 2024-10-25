package diary.api;

import diary.dto.DiaryDetailResponse;
import diary.dto.DiaryRequest;
import diary.dto.DiaryResponse;
import diary.dto.Diary;
import diary.repository.DiaryEntity;
import diary.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
    Flux<DiaryResponse> getAll() {
        return diaryService.getAllDiary()
                .flatMap(diary -> Flux.just(new DiaryResponse(diary.id(), diary.title(), diary.category())));
    }

    @GetMapping("/api/diary")
    Flux<DiaryResponse> getRecent() {
        return diaryService.getRecentDiary()
                .flatMap(diary -> Flux.just(new DiaryResponse(diary.id(), diary.title(), diary.category())));
    }

    @GetMapping("/api/diary/sorted")
    Flux<DiaryResponse> getSorted() {
        return diaryService.getSortedDiary()
                .flatMap(diary -> Flux.just(new DiaryResponse(diary.id(), diary.title(), diary.category())));
    }

    @GetMapping("/api/diary/category/{categoryId}")
    Flux<DiaryResponse> getCategory(@PathVariable Integer categoryId) {
        DiaryEntity.Category category = DiaryEntity.Category.values()[categoryId];
        return diaryService.getDiariesByCategory(category)
                .flatMap(diary -> Flux.just(new DiaryResponse(diary.id(), diary.title(), diary.category())));
    }

    @GetMapping("/api/diary/{id}")
    ResponseEntity<DiaryDetailResponse> getById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);
        return ResponseEntity.ok(new DiaryDetailResponse(diary.id(), diary.title(), diary.content(), diary.date(), diary.category()));
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
