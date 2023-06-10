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

    public Bookmark save(Long user_id, CreateForm createForm){
        Bookmark bookmark = new Bookmark();
        bookmark.setSiteUrl(createForm.getSiteUrl());
        bookmark.setExplanation(createForm.getExplanation());
        Optional<User> user = userRepository.findById(user_id);
        Optional<Directory> directory = directoryRepository.findById(createForm.getDirectory_id());
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
        bookmarkRepository.update(id, updateForm);
    }

    public List<Bookmark> findAll(Long user_id){
        List<Bookmark> bookmarks = bookmarkRepository.findAll(user_id);
        return bookmarks;
    }

    public Optional<Bookmark> findById(Long id){
        Optional<Bookmark> bookmark = bookmarkRepository.findById(id);
        return bookmark;
    }

    public void delete(Long id){
        Optional<Bookmark> bookmark = findById(id);
        if(bookmark.isEmpty()) return;
        bookmarkRepository.delete(bookmark.get());
    }
}