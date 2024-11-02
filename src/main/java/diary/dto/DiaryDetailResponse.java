package diary.dto;

import diary.repository.DiaryEntity;
import java.time.LocalDateTime;

public record DiaryDetailResponse(long id, String title, String content, LocalDateTime date, DiaryEntity.Category category, boolean isVisible, long userId, String nickname) {
}
