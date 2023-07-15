package project.bookmark.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Form.DirectoryForm;
import project.bookmark.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DirectoryServiceTest {
    @Autowired DirectoryService directoryService;
    @Autowired UserRepository userRepository;

    @Test
    public void firstTest() throws Exception {
        // given
        User user1=new User();
        user1.setUsername("han1");
        user1.setEmail("gks1@ggg");
        user1.setRole(Role.ROLE_USER);
        User user2=new User();
        user2.setUsername("han2");
        user2.setEmail("gks2@ggg");
        user2.setRole(Role.ROLE_USER);
        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> saved1 = userRepository.findByUsername("han1");
        Optional<User> saved2 = userRepository.findByUsername("han2");
        Long user_id1 = saved1.get().getId();
        Long user_id2 = saved2.get().getId();

        DirectoryForm directoryForm1 = new DirectoryForm();
        directoryForm1.setName("new folder1");
        DirectoryForm directoryForm2 = new DirectoryForm();
        directoryForm2.setName("new folder2");

        directoryService.save(user_id1, directoryForm1);
        directoryService.save(user_id2, directoryForm2);

        // when
        List<Directory> all1 = directoryService.findByUserId(user_id1);
        List<Directory> all2 = directoryService.findByUserId(user_id2);

        // then
        System.out.println("------------------------------------------");
        for(int i=0;i<all1.size();i++)
            System.out.print(all1.get(i) + " ");
        System.out.println();
        System.out.println("------------------------------------------");
        for(int i=0;i<all2.size();i++)
            System.out.println(all2.get(i) + " ");
        System.out.println();

    }

    @Test
    public void fetchJoinTest() throws Exception {
        // given

        // when

    }

}