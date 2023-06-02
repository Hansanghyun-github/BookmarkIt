package project.bookmark.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Repository.BookmarkRepository;
import project.bookmark.Repository.BookmarkSearchCond;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestService {
    BookmarkRepository bookmarkRepository;

    @Autowired
    public TestService(@Qualifier("testRepository") BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public Bookmark save(CreateForm createForm){
        Bookmark bookmark = new Bookmark();
        bookmark.setSiteUrl(createForm.getSiteUrl());
        bookmark.setExplanation(createForm.getExplanation());
        return bookmarkRepository.save(bookmark);
    }

    public void update(Long id, UpdateForm updateForm){
        bookmarkRepository.update(id, updateForm);
    }

    public List<Bookmark> findAll(){
        BookmarkSearchCond bookmarkSearchCond = new BookmarkSearchCond();
        List<Bookmark> bookmarks = bookmarkRepository.findAll(0L);
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
