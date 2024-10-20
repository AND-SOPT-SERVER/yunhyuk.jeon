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

    public DiaryEntity(){
    }

    public DiaryEntity(String title, String content, LocalDateTime date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }
}
