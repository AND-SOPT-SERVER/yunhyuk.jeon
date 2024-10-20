package diary.service;

import java.time.LocalDateTime;

public class Diary {

    private final long id;
    private final String title;
    private final String content;
    private final LocalDateTime date;

    public Diary(long id, String title, String content, LocalDateTime date){
        this.id = id;
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
}
