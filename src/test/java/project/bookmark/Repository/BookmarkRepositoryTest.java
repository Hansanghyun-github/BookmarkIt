package project.bookmark.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.QBookmark;
import project.bookmark.Domain.QDirectory;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static project.bookmark.Domain.QBookmark.bookmark;
import static project.bookmark.Domain.QDirectory.directory;

@SpringBootTest
@Transactional
class BookmarkRepositoryTest {
    EntityManager em;
    JPAQueryFactory query;
    DirectoryRepository directoryRepository;

    List<Long> directoryIds = new ArrayList<>();
    List<Long> bookmarkIds = new ArrayList<>();

    @Autowired
    public BookmarkRepositoryTest(EntityManager em, DirectoryRepository directoryRepository) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
        this.directoryRepository = directoryRepository;
    }

    @BeforeEach
    public void testData(){
        Directory directory1 = new Directory();
        directory1.setName("root");
        Directory directory2 = new Directory();
        directory2.setName("folder1");
        Directory directory3 = new Directory();
        directory3.setName("folder2");

        Directory save1 = directoryRepository.save(directory1);
        save1.setPrevDirectoryId(save1.getId());
        Directory save2 = directoryRepository.save(directory2);
        save2.setPrevDirectoryId(save2.getId());
        Directory save3 = directoryRepository.save(directory3);
        save3.setPrevDirectoryId(save3.getId());

        directoryIds.add(save1.getId());
        directoryIds.add(save2.getId());
        directoryIds.add(save3.getId());

        Bookmark bookmark1 = new Bookmark();
        bookmark1.setName("google");
        bookmark1.setUrl("https://www.google.com");
        bookmark1.setDirectory(save1);

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setName("google1");
        bookmark2.setUrl("https://www.google.com1");
        bookmark2.setDirectory(save1);

        Bookmark bookmark3 = new Bookmark();
        bookmark3.setName("google2");
        bookmark3.setUrl("https://www.google.com2");
        bookmark3.setDirectory(save2);

        em.persist(bookmark1);
        em.persist(bookmark2);
        em.persist(bookmark3);
        em.flush();
        em.clear();

        assertThat(bookmark1).isIn(directory1.getBookmarks());
        assertThat(bookmark2).isIn(directory1.getBookmarks());
        assertThat(bookmark3).isIn(directory2.getBookmarks());
    }

    @Test
    public void fetchJoinTest() throws Exception {
        // given
        List<Bookmark> resultList =
                em.createQuery("select b from Bookmark b join fetch b.directory d",
                                Bookmark.class).getResultList();

        // when
        System.out.println("-----------------before find all directories-------------------");
        List<Directory> directories = directoryRepository.findAll();

        // then
        resultList.forEach(b -> System.out.println(b));
        directories.forEach(d -> System.out.println(d));

    }

    @Test
    public void querydslFetchJoinTest() throws Exception {
        // given
        List<Bookmark> bookmarks = query.select(bookmark)
                .from(bookmark)
                .join(bookmark.directory, directory).fetchJoin()
                .fetch();

        // when
        bookmarks.forEach(b -> System.out.println(b));

        // then

    }

    @Test
    public void fetchJoinTest2() throws Exception {
        // given
        List<Directory> directories = query.select(directory)
                .from(directory)
                .join(directory.bookmarks).fetchJoin()
                .fetch();

        // when
        directories.forEach(d -> System.out.println(d));
        directories.forEach(d -> {
            d.getBookmarks().forEach(b -> System.out.println(b));
            System.out.println();
        });

        // then

    }

    @Test
    public void fetchJoinTest3() throws Exception {
        // given
        List<Directory> directories = query.select(directory).distinct()
                .from(directory)
                .join(directory.bookmarks).fetchJoin()
                .fetch();

        // when
        directories.forEach(d -> System.out.println(d));
        directories.forEach(d -> {
            d.getBookmarks().forEach(b -> System.out.println(b));
            System.out.println();
        });

        // then
            Bookmark bookmark1 = em.find(Bookmark.class, directories.get(0).getBookmarks().get(0).getId());

                System.out.println(bookmark1);

    }

    @Test
    public void InitDataTest() throws Exception {
        // given
        List<Bookmark> fetch = query.select(bookmark)
                .from(bookmark).fetch();

        List<Directory> directories = query.select(directory).from(directory).fetch();
        // when
        fetch.forEach(b -> System.out.println(b));
        directories.forEach(d -> System.out.println(d));

        // then

    }

    @Test
    public void teaf() throws Exception {
        // given
        List<Directory> directories = directoryRepository.findBookmarksAndDirectories();

        // then
        directories.forEach(d -> System.out.println(d));

        System.out.println();
        directories.forEach(d -> {
            d.getBookmarks().forEach(b -> System.out.println(b));
            System.out.println();
        });

        // when

        // then

    }
}