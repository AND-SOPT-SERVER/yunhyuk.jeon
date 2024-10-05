package org.sopt.week1;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DiaryRepository {
    private final Map<Long, String> storage = new ConcurrentHashMap<>();
    private final AtomicLong numbering = new AtomicLong();

    public Map<Long, String> findAll() {
        return storage;
    }

    public Long save(String content) {
        Long id = numbering.incrementAndGet();
        storage.put(id, content);
        return id;
    }

    public boolean deleteById(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return true;
        }
        return false;
    }

    public boolean update(Long id, String newContent) {
        if (storage.containsKey(id)) {
            storage.put(id, newContent);
            return true;
        }
        return false;
    }

    public Optional<String> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
