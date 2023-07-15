package project.bookmark.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Form.UpdateForm;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
@Primary
public class JPABookmarkRepository implements BookmarkRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JPABookmarkRepository(EntityManager em) {
            this.em = em;
            this.query = new JPAQueryFactory(em);
    }

    @Override
    public Bookmark save(Bookmark bookmark){
        em.persist(bookmark);
        return bookmark;
    }

    @Override
    public void delete(Long id) {
        Bookmark bookmark = em.find(Bookmark.class, id);
        em.remove(bookmark);
    }

    @Override
    public void update(Long bookmarkId, UpdateForm editForm) {
        Bookmark bookmark = em.find(Bookmark.class, bookmarkId);
        bookmark.setUrl(editForm.getUrl());
        bookmark.setName(editForm.getName());
    }

    @Override
    public Optional<Bookmark> findById(Long id){
        Bookmark bookmark = em.find(Bookmark.class, id);
        return Optional.ofNullable(bookmark);
    }

    @Override
    public List<Bookmark> findAll() {
        return em.createQuery("select b from Bookmark b")
                .getResultList();
    }

    @Override
    public List<Bookmark> findAll(Long user_id) {
        return em.createQuery("select b from Bookmark b where b.user.id = :id", Bookmark.class)
                .setParameter("id", user_id)
                .getResultList();
    }
}
