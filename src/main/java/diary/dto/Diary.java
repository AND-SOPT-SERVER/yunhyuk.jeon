package diary.dto;

import java.time.LocalDateTime;

public record Diary(long id, String title, String content, LocalDateTime date) {
}
