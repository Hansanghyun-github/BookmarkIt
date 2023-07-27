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

    // TODO 인증,인가 기능 추가후 삭제
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

    public List<Directory> findDirectoriesAndBookmarks(Long user_id) {
        return directoryRepository.findBookmarksAndDirectories(user_id);
    }

    public List<Directory> findByUserId(Long user_id){
        return directoryRepository.findByUserId(user_id);
    }

    public void delete(Long id){
        directoryRepository.deleteById(id);
    }

    // TODO 이미 앞에서 유저 확인했으니 또 find 할필요없음
    public void createTwoDirectoryForNewUser(Long user_id){
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()){

            return;
        }

        Directory directory1 = new Directory();
        directory1.setName("root");
        directory1.setUser(user.get());
        Directory save1 = directoryRepository.save(directory1);
        save1.setPrevDirectoryId(save1.getId());

        Directory directory2 = new Directory();
        directory2.setName("sub folder");
        directory2.setUser(user.get());
        Directory save2 = directoryRepository.save(directory2);
        save2.setPrevDirectoryId(save2.getId());
    }

    public boolean isInvalidDirectoryId(Long id, Long user_id){
        Optional<Directory> directory = directoryRepository.findByIdEntityGraph(id);
        if(directory.isEmpty())
            return true;
        if(directory.isPresent() && directory.get().getUser().getId() != user_id)
            return true;
        return false;
    }
}
