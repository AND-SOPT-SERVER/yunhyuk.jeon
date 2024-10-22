package diary.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String title;
    public String content;
    public LocalDateTime date;

    @Enumerated(EnumType.STRING)
    public Category category;

    public DiaryEntity() {}

    public DiaryEntity(String title, String content, LocalDateTime date, Category category) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    public enum Category {
        DAILY, FOOD, EXERCISE, WORK, STUDY, OTHER
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
