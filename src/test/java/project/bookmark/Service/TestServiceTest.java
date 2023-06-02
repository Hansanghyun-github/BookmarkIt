package project.bookmark.Service;

import org.assertj.core.api.Assertions;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {
    @Autowired
    TestService testService;
    @Test
    public void 생성테스트() throws Exception {
        // given
        CreateForm createForm1 = new CreateForm();
        createForm1.setExplanation("explain1");
        createForm1.setSiteUrl("site1");
        Bookmark save1 = testService.save(createForm1);

        CreateForm createForm2 = new CreateForm();
        createForm2.setExplanation("explain2");
        createForm2.setSiteUrl("site2");
        Bookmark save2 = testService.save(createForm2);

        CreateForm createForm3 = new CreateForm();
        createForm3.setExplanation("explain3");
        createForm3.setSiteUrl("site3");
        Bookmark save3 = testService.save(createForm3);

        // when
        List<Bookmark> bookmarks = testService.findAll();

        // then
        for(int i=0;i<bookmarks.size();i++)
            System.out.println(bookmarks.get(i));

        // when
        UpdateForm updateForm=new UpdateForm();
        updateForm.setSiteUrl("new site");
        updateForm.setExplanation("new explain");
        testService.update(save2.getId(), updateForm);

        testService.delete(save1.getId());
        Assertions.assertThat(testService.findById(save1.getId())).isEmpty();

        bookmarks = testService.findAll();

        // then
        System.out.println();
        for(int i=0;i<bookmarks.size();i++)
            System.out.println(bookmarks.get(i));
    }
}