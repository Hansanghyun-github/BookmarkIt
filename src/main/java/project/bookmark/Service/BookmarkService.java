package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.User;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Repository.BookmarkRepository;
import project.bookmark.Repository.DirectoryRepository;
import project.bookmark.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BookmarkService {
    final BookmarkRepository bookmarkRepository;
    final UserRepository userRepository;
    final DirectoryRepository directoryRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, DirectoryRepository directoryRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.directoryRepository = directoryRepository;
    }

    public Bookmark save(CreateForm createForm){
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(createForm.getUrl());
        bookmark.setName(createForm.getName());
        Optional<Directory> directory = directoryRepository.findById(createForm.getDirectoryId());
        if(directory.isEmpty()){
            log.warn("save 중 해당 디렉토리 객체 없음");
            return null;
        }
        bookmark.setDirectory(directory.get());
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark save(Long user_id, CreateForm createForm){
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(createForm.getUrl());
        bookmark.setName(createForm.getName());
        Optional<User> user = userRepository.findById(user_id);
        Optional<Directory> directory = directoryRepository.findById(createForm.getDirectoryId());
        if(user.isEmpty()){
            log.warn("save 중 해당 유저 객체 없음");
            return null;
        }
        if(directory.isEmpty()){
            log.warn("save 중 해당 디렉토리 객체 없음");
            return null;
        }
        bookmark.setUserAndDirectory(user.get(), directory.get());
        return bookmarkRepository.save(bookmark);
    }

    public void update(Long id, UpdateForm updateForm){
        Optional<Bookmark> byId = bookmarkRepository.findById(id);
        if(byId.isEmpty()) return;
        Bookmark bookmark = byId.get();
        bookmark.setUrl(updateForm.getUrl());
        bookmark.setName(updateForm.getName());
    }

    public List<Bookmark> findAll() {
        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        Directory directory;
        for(int i=0;i<bookmarks.size();i++){
            directory = bookmarks.get(i).getDirectory();
            log.info("\n" + directory);
        }
        return bookmarks;
    }

    public List<Bookmark> findAll(Long user_id){
        // TODO using user_id
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(user_id);
        log.info("call bookmark's directory because of LAZY initialization");
        Directory directory;
        for(int i=0;i<bookmarks.size();i++){
            directory = bookmarks.get(i).getDirectory();
            log.info("\n" + directory);
        }
        return bookmarks;
    }

    public Optional<Bookmark> findById(Long id){
        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);
        return bookmark;
    }

    public void delete(Long id){
        bookmarkRepository.deleteById(id);
    }

    public boolean isInvalidBookmarkId(Long id){
        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);
        return bookmark.isEmpty();
    }
}