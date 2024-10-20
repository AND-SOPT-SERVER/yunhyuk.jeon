package diary.dto;

import java.time.LocalDateTime;

public record DiaryDetailResponse(Long id, String title, String content, LocalDateTime date) {
}
