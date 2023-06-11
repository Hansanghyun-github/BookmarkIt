package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Form.UserForm;
import project.bookmark.Repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {
    final private UserRepository userRepository;
    //final private DirectoryService directoryService;

    @Autowired
    public UserService(UserRepository userRepository/*, DirectoryService directoryService*/) {
        this.userRepository = userRepository;
        //this.directoryService = directoryService;
    }

    public User save(UserForm userForm){
        User user = User.builder()
                .username(userForm.getUsername())
                .password(userForm.getPassword())
                .email(userForm.getEmail())
                .role(Role.ROLE_USER)
                .bookmarks(new ArrayList<>())
                //.directories(new ArrayList<>())
                .build();

        User save = userRepository.save(user);
        //directoryService.createRootDirectory(save.getId());

        return save;
    }

    public boolean isDuplicate(String username){
        Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent();
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
