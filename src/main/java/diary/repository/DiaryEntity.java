package diary.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diary")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    public UserEntity user;

    @Column(unique = true)
    public String title;

    public String content;
    public LocalDateTime date;

    @Enumerated(EnumType.STRING)
    public Category category;

    @Column(name = "is_visible", nullable = false)
    public boolean isVisible;

    public DiaryEntity() {}

    public DiaryEntity(UserEntity user, String title, String content, LocalDateTime date, Category category, boolean isVisible) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.date = date;
        this.category = category;
        this.isVisible = isVisible;
    }

    public enum Category {
        FOOD, SCHOOL, MOVIE, EXERCISE
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

    public boolean getIsVisible() {
        return isVisible;
    }

    public UserEntity getUser() {
        return user;
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
