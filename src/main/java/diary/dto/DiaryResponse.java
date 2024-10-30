package diary.dto;

import diary.repository.DiaryEntity;

import java.time.LocalDateTime;

public record DiaryResponse(long id, String title, String username, LocalDateTime date, DiaryEntity.Category category, boolean isVisible) {
}
