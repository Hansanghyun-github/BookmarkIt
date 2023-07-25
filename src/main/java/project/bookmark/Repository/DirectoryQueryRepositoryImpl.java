package project.bookmark.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.QBookmark;
import project.bookmark.Domain.QDirectory;

import javax.persistence.EntityManager;
import java.util.List;

import static project.bookmark.Domain.QBookmark.bookmark;
import static project.bookmark.Domain.QDirectory.directory;

public class DirectoryQueryRepositoryImpl implements DirectoryQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory query;

    @Autowired
    public DirectoryQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Directory> findBookmarksAndDirectories(Long user_id) {
        return query.select(directory).distinct()
                .from(directory)
                .leftJoin(directory.bookmarks).fetchJoin()
                .where(directory.user.id.eq(user_id))
                .fetch();
    }
}
