package project.bookmark.Repository;

import org.springframework.stereotype.Repository;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.User;
import project.bookmark.Form.UpdateForm;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository {
    Bookmark save(Bookmark bookmark);

    void update(Long bookmarkId, UpdateForm editForm);

    void delete(Long id);

    Optional<Bookmark> findById(Long id);

    List<Bookmark> findAll();

    List<Bookmark> findAll(Long user_id);

}
