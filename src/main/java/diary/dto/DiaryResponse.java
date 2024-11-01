package diary.dto;

import diary.repository.DiaryEntity;

import java.time.LocalDateTime;

public record DiaryResponse(long id, String title, LocalDateTime date, DiaryEntity.Category category, boolean isVisible) {
}
