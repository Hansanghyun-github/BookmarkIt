package project.bookmark.Repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Form.UpdateForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository()
@Slf4j
@Qualifier("testRepository")
public class TestRepository implements BookmarkRepository{

    private HashMap<Long, Bookmark> store = new HashMap<>();

    private static long count = 1L;

    @Override
    public Bookmark save(Bookmark bookmark) {
        bookmark.setId(++count);
        store.put(bookmark.getId(), bookmark);
        return bookmark;
    }

    @Override
    public List<Bookmark> findAll() {
        return null;
    }

    @Override
    public void update(Long bookmarkId, UpdateForm editForm) {
        Bookmark bookmark = store.get(bookmarkId);
        if(bookmark == null) return;

        store.remove(bookmarkId);

        bookmark.setUrl(editForm.getUrl());
        bookmark.setName(editForm.getName());
        store.put(bookmark.getId(),bookmark);
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    @Override
    public Optional<Bookmark> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Bookmark> findAll(Long user_id) { return new ArrayList<>(store.values()); }

    public void clearStore() {
        store.clear();
    }
}
