package diary.dto;

import diary.repository.DiaryEntity;

public record DiaryRequest(String title, String content, DiaryEntity.Category category, Boolean isVisible) {
    public void validate() {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (category == null) {
            throw new IllegalArgumentException("카테고리를 입력해주세요.");
        }
        if (isVisible == null) {
            throw new IllegalArgumentException("공개 여부를 설정해주세요.");
        }
    }
}
