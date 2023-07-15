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

    public Directory save(DirectoryForm directoryForm){

        Directory directory=new Directory();
        directory.setName(directoryForm.getName());
        directory.setPrevDirectoryId(directoryForm.getPrevDirectoryId());
        return directoryRepository.save(directory);
    }

    public Directory save(Long user_id, DirectoryForm directoryForm){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()){

            return null;
        }

        Directory directory=new Directory();
        directory.setName(directoryForm.getName());
        directory.setPrevDirectoryId(directoryForm.getPrevDirectoryId());
        directory.setUser(user.get());
        return directoryRepository.save(directory);
    }

    public void update(Long id, DirectoryForm directoryForm){
        Optional<Directory> directory = directoryRepository.findById(id);
        if(directory.isEmpty()){
            return;
        }
        directory.get().setName(directoryForm.getName());
        directory.get().setPrevDirectoryId(directoryForm.getPrevDirectoryId());
    }

    public Optional<Directory> findById(Long id){
        return directoryRepository.findById(id);
    }

    public List<Directory> findAll(){
        return directoryRepository.findAll();
    }

    public List<Directory> findByUserId(Long user_id){
        return directoryRepository.findByUserId(user_id);
    }

    public void delete(Long id){
        directoryRepository.deleteById(id);
    }

    public Long createRootDirectory(Long user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()){

            return null;
        }

        Directory directory = new Directory();
        directory.setName("root");
        directory.setUser(user.get());
        Directory save = directoryRepository.save(directory);
        save.setPrevDirectoryId(save.getId());

        return save.getId();
    }

    public boolean isInvalidDirectoryId(Long id){
        Optional<Directory> directory = directoryRepository.findById(id);
        return directory.isEmpty();
    }
}
