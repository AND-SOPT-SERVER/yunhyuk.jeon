package diary.dto;

import diary.repository.DiaryEntity;

public record DiaryResponse(long id, String title, DiaryEntity.Category category) {
}
