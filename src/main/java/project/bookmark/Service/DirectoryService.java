package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bookmark.Domain.Directory;
import project.bookmark.Repository.DirectoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DirectoryService {
    private DirectoryRepository directoryRepository;

    @Autowired
    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public void save(){

    }

    public void update(){

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
