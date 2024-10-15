package diary.api;


import diary.service.Diary;
import diary.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    @PostMapping("/post")
    void post(){
        diaryService.createDiary();
    }

    @GetMapping("/post")
    ResponseEntity<DiaryListResponse> get(){
        List<Diary> diaryList = diaryService.getList();

        List<DiaryResponse> diaryResponseList = new ArrayList<>();
        for(Diary diary : diaryList){
            diaryResponseList.add(new DiaryResponse(diary.getId(), diary.getName()));
        }
        return ResponseEntity.ok(new DiaryListResponse(diaryResponseList));
    }
}
