package diary.dto;

import diary.repository.DiaryEntity;

import java.time.LocalDateTime;

public record DiaryDetailResponse(Long id, String title, String content, LocalDateTime date, DiaryEntity.Category category) {
}
