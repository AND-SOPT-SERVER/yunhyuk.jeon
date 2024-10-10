package org.sopt.week1;

import java.util.List;

public class DiaryController {
    private Status status = Status.READY;
    private final DiaryService diaryService = new DiaryService();

    Status getStatus() {
        return status;
    }

    void boot() {
        this.status = Status.RUNNING;
    }

    void finish() {
        this.status = Status.FINISHED;
    }

    final List<String> getList() {
        return diaryService.getDiary();
    }

    final void post(final String body) {
        diaryService.createDiary(body);
    }

    final void delete(final String id) {
        Long newID = Long.parseLong(id);
        diaryService.deleteDiary(newID);
    }

    final void patch(final String id, final String body) {
        Long newID = Long.parseLong(id);
        diaryService.updateDiary(newID, body);
    }

    final void restore(final String id) {
        Long newID = Long.parseLong(id);
        diaryService.restoreDiary(newID);
    }

    enum Status {
        READY,
        RUNNING,
        FINISHED,
        ERROR,
    }
}
