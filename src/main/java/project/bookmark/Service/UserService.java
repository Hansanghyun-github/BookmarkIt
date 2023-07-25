package project.bookmark.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Form.UserForm;
import project.bookmark.Repository.UserRepository;
import project.bookmark.dto.JoinRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DirectoryService directoryService;
    private final PasswordEncoder passwordEncoder;
    public Long save(JoinRequestDto joinRequestDto) {
        User user=new User(
                joinRequestDto.getUsername(),
                passwordEncoder.encode(joinRequestDto.getPassword())
        );
        return userRepository.save(user).getId();
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
