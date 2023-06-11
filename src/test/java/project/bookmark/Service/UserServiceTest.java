package project.bookmark.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
//import project.bookmark.Domain.Directory;
import project.bookmark.Domain.User;
import project.bookmark.Form.UserForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired UserService userService;
    //@Autowired DirectoryService directoryService;
    @Test
    public void userTest() throws Exception {
        // given
        UserForm userForm1=UserForm.builder()
                .username("han1")
                .email("gks@gks")
                .build();
        User save1 = userService.save(userForm1);

        UserForm userForm2=UserForm.builder()
                .username("han2")
                .email("gks@gks")
                .build();
        User save2 = userService.save(userForm2);

        // when
        List<User> all = userService.findAll();
        //List<Directory> directories = directoryService.findAll();

        // then
        System.out.println(all);
        //System.out.println(directories);

        System.out.println(userService.isDuplicate("han1"));
        System.out.println(userService.isDuplicate("han0"));
        System.out.println(userService.isDuplicate("han2"));

    }
}