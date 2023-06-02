package project.bookmark.Service;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Repository.UserRepository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookmarkServiceTest {
    // TODO 왜 유저는 바로 세이브 되고 북마크는 바로 세이브 되지않을까 영속성이랑 관련있는듯
    @Autowired
    BookmarkService testService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void 미리미리(){

        User user1= User.builder()
                .username("han")
                .role(Role.ROLE_USER)
                .email("gks@gks")
                .bookmarks(new ArrayList<>())
                .build();
        User user2= User.builder()
                .username("sang")
                .role(Role.ROLE_USER)
                .email("gks@gks")
                .bookmarks(new ArrayList<>())
                .build();
        System.out.println("------------------ before user save -------------------");
        User saved1 = userRepository.save(user1);
        User saved2 = userRepository.save(user2);
        System.out.println("------------------ after user save -------------------");
        CreateForm createForm1 = new CreateForm();
        createForm1.setExplanation("explain1");
        createForm1.setSiteUrl("site1");
        System.out.println(saved1);
        System.out.println(saved2);
        System.out.println(saved1.getBookmarks());
        System.out.println("------------------ before bookmark save -------------------");
        Bookmark save1 = testService.save(saved1.getId(), createForm1);

        CreateForm createForm2 = new CreateForm();
        createForm2.setExplanation("explain2");
        createForm2.setSiteUrl("site2");
        Bookmark save2 = testService.save(saved1.getId(), createForm2);

        CreateForm createForm3 = new CreateForm();
        createForm3.setExplanation("explain3");
        createForm3.setSiteUrl("site3");
        Bookmark save3 = testService.save(saved2.getId(), createForm3);
        System.out.println("------------------ after bookmark save -------------------");
    }
    @Test
    @Transactional
    public void 생성테스트() throws Exception {
        // given
        User user1= User.builder()
                .username("han")
                .role(Role.ROLE_USER)
                .email("gks@gks")
                .bookmarks(new ArrayList<>())
                .build();
        User user2= User.builder()
                .username("sang")
                .role(Role.ROLE_USER)
                .email("gks@gks")
                .bookmarks(new ArrayList<>())
                .build();
        User saved1 = userRepository.save(user1);
        User saved2 = userRepository.save(user2);
        CreateForm createForm1 = new CreateForm();
        createForm1.setExplanation("explain1");
        createForm1.setSiteUrl("site1");
        System.out.println(saved1);
        System.out.println(saved2);
        System.out.println(saved1.getBookmarks());
        Bookmark save1 = testService.save(saved1.getId(), createForm1);

        CreateForm createForm2 = new CreateForm();
        createForm2.setExplanation("explain2");
        createForm2.setSiteUrl("site2");
        Bookmark save2 = testService.save(saved1.getId(), createForm2);

        CreateForm createForm3 = new CreateForm();
        createForm3.setExplanation("explain3");
        createForm3.setSiteUrl("site3");
        Bookmark save3 = testService.save(saved2.getId(), createForm3);

        // when
        List<Bookmark> bookmarks = testService.findAll(saved1.getId());
        List<Bookmark> bookmarks2 = testService.findAll(saved2.getId());

        // then
        System.out.println("user1: ");
        for(int i=0;i<bookmarks.size();i++)
            System.out.println(bookmarks.get(i));
        System.out.println("user2: ");
        for(int i=0;i<bookmarks2.size();i++)
            System.out.println(bookmarks2.get(i));

        // when
        UpdateForm updateForm=new UpdateForm();
        updateForm.setSiteUrl("new site");
        updateForm.setExplanation("new explain");
        testService.update(save2.getId(), updateForm);

        testService.delete(save1.getId());
        Assertions.assertThat(testService.findById(save1.getId())).isEmpty();

        // when
        bookmarks = testService.findAll(saved1.getId());
        bookmarks2 = testService.findAll(saved2.getId());

        // then
        System.out.println("user1: ");
        for(int i=0;i<bookmarks.size();i++)
            System.out.println(bookmarks.get(i));
        System.out.println("user2: ");
        for(int i=0;i<bookmarks2.size();i++)
            System.out.println(bookmarks2.get(i));
    }

    @Test
    @Transactional
    public void 추가테스트() throws Exception {
        // given
        List<User> users = userRepository.findAll();
        System.out.println("----------- user get ---------------");
        List<Bookmark> b1 = testService.findAll(users.get(0).getId());
        List<Bookmark> b2 = testService.findAll(users.get(1).getId());
        System.out.println("----------- bookmark get ---------------");

        for(int i=0;i<b1.size();i++)
            System.out.println(b1.get(i));
        System.out.println();
        for(int i=0;i<b2.size();i++)
            System.out.println(b2.get(i));

        // when

        // then

    }

}