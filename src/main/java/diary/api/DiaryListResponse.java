package diary.api;


import java.util.List;

public class DiaryListResponse {
    private List<DiaryResponse> diaryList;

    public DiaryListResponse(List<DiaryResponse> diaryListResponse){
        this.diaryList = diaryListResponse;
    }

    public List<DiaryResponse> getDiaryList(){
        return diaryList;
    }
}
