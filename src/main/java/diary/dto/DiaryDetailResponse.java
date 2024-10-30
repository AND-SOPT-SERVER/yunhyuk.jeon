package diary.dto;

import diary.repository.DiaryEntity;
import java.time.LocalDateTime;

public record DiaryDetailResponse(long id, String title, String content, String username, LocalDateTime date, DiaryEntity.Category category, boolean isVisible) {
}
