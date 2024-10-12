package org.sopt.week1;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;

public class DiaryRepository {
    private final Map<Long, String> storage = new ConcurrentHashMap<>();
    private final Map<Long, String> trash = new ConcurrentHashMap<>();
    private final AtomicLong numbering = new AtomicLong();
    private final String storagePath = "diary.txt";
    private final String trashPath = "trash.txt";
    private final String updateCountPath = "updateCount.txt";

    public DiaryRepository() {
        loadFile();
    }

    private void loadFile() {
        numbering.set(max(loadStorage(),loadTrash()));
    }

    private Long loadStorage(){
        File file = new File(storagePath);
        if (!file.exists()) {
            return 0L;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(storagePath))) {
            String line;
            Long last = null;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(":", 2);
                if (parts.length == 2){
                    Long id = Long.parseLong(parts[0]);
                    String content = parts[1];
                    storage.put(id, content);
                    last = id;
                }
            }
            if (last != null){
                return last;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }

    private Long loadTrash() {
        File file = new File(trashPath);
        if (!file.exists()){
            return 0L;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(trashPath))){
            String line;
            Long last = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2){
                    Long id = Long.parseLong(parts[0]);
                    String content = parts[1];
                    trash.put(id,content);
                    last = id;
                }
                if (last != null){
                    return last;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }

    private void saveStorage() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storagePath))) {
            for (Map.Entry<Long, String> entry : storage.entrySet()){
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } ;
    }

    private void saveTrash() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(trashPath))){
            for (Map.Entry<Long, String> entry : trash.entrySet()){
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Long save(String content) {
        Long id = numbering.incrementAndGet();
        storage.put(id, content);
        saveStorage();
        return id;
    }

    public boolean deleteById(Long id) {
        if (storage.containsKey(id)) {
            trash.put(id, storage.remove(id));
            saveStorage();
            saveTrash();
            return true;
        }
        return false;
    }

    public LocalDate readUpdateDate() throws IOException {
        File file = new File(updateCountPath);
        if (!file.exists()) return null;

        BufferedReader reader = new BufferedReader(new FileReader(updateCountPath));
        String firstLine = reader.readLine();
        reader.close();

        if (firstLine == null || firstLine.trim().isEmpty()) {
            return null;
        }

        return LocalDate.parse(firstLine, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void clearUpdateCount() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(updateCountPath, false));
        writer.write("");
        writer.close();
    }

    public void writeUpdateCount(LocalDate date) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(updateCountPath, true));
        writer.write(date.toString() + "\n");
        writer.close();
    }

    public long getUpdateCount() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(updateCountPath));
        long count = reader.lines().count();
        reader.close();
        return count;
    }

    public boolean update(Long id, String newContent) throws IOException {
        if (!storage.containsKey(id)) return false;

        storage.put(id, newContent);
        saveStorage();
        writeUpdateCount(LocalDate.now());
        return true;
    }

    public boolean restore(Long id) {
        if (trash.containsKey(id)){
            String content = trash.remove(id);
            storage.put(id,content);
            saveStorage();
            saveTrash();
            return true;
        }
        return false;
    }

}
