package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.User;
import project.bookmark.Form.DirectoryForm;
import project.bookmark.Repository.DirectoryRepository;
import project.bookmark.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class DirectoryService {
    private DirectoryRepository directoryRepository;
    private UserRepository userRepository;
    @Autowired
    public DirectoryService(DirectoryRepository directoryRepository, UserRepository userRepository) {
        this.directoryRepository = directoryRepository;
        this.userRepository = userRepository;
    }

    public void save(Long user_id, DirectoryForm directoryForm){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()){

            return;
        }

        Directory directory=new Directory();
        directory.setDirectoryName(directoryForm.getDirectoryName());
        directory.setPrevDirectoryId(directoryForm.getPrevDirectoryId());
        directory.setUser(user.get());
        directoryRepository.save(directory);
    }

    public void update(Long id, DirectoryForm directoryForm){
        Optional<Directory> directory = directoryRepository.findById(id);
        if(directory.isEmpty()){
            return;
        }
        directory.get().setDirectoryName(directoryForm.getDirectoryName());
        directory.get().setPrevDirectoryId(directoryForm.getPrevDirectoryId());
    }

    public Optional<Directory> findById(Long id){
        return directoryRepository.findById(id);
    }

    public List<Directory> findAll(Long user_id){
        return directoryRepository.findByUserId(user_id);
    }

    public void delete(Long id){
        directoryRepository.deleteById(id);
    }
}
