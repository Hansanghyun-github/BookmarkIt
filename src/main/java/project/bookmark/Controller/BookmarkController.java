package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.ToRest.ListForm;
import project.bookmark.Form.ToRest.TBookmark;
import project.bookmark.Form.ToRest.TDirectory;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Service.BookmarkService;
import project.bookmark.Service.DirectoryService;
import project.bookmark.advice.ValidationError;

import java.util.*;

@RestController
@CrossOrigin
@Slf4j
public class BookmarkController {
    final BookmarkService bookmarkService;
    final DirectoryService directoryService;
    @Autowired
    public BookmarkController(BookmarkService bookmarkService, DirectoryService directoryService) {
        this.bookmarkService = bookmarkService;
        this.directoryService = directoryService;
    }

    @GetMapping("/bookmarks")
    public ListForm bookmarks(Authentication authentication) {
        log.info("bookmark list");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long user_id = principalDetails.getUser().getId();

        List<Directory> directories = directoryService.findDirectoriesAndBookmarks(user_id);

        List<TBookmark> tBookmarks = new ArrayList<>();
        List<TDirectory> tDirectories = new ArrayList<>();

        directories.forEach(d -> d.getBookmarks().forEach(it -> {
            TBookmark tBookmark = TBookmark.builder()
                    .id(it.getId())
                    .url(it.getUrl())
                    .name(it.getName())
                    .directoryId(it.getDirectory().getId())
                    .build();
            tBookmarks.add(tBookmark);
        }));
        for(Directory it:directories) {
            TDirectory tDirectory = TDirectory.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .prevDirectoryId(it.getPrevDirectoryId())
                    .build();
            tDirectories.add(tDirectory);
        }

        ListForm listForm = new ListForm();
        listForm.setBookmarks(tBookmarks);
        listForm.setDirectories(tDirectories);

        return listForm;
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<Object> create(
            Authentication authentication,
            @Validated @RequestBody CreateForm createForm,
            BindingResult bindingResult){
        log.info("bookmark create");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long user_id = principalDetails.getUser().getId();

        if(directoryService.isInvalidDirectoryId(createForm.getDirectoryId(), user_id)){
            bindingResult.addError(
                    new FieldError(
                            "CreateFrom",
                            "directoryId",
                            "Invalid Directory Id"));
        }

        // TODO directoryId TypeMismatchException Handler
        if(bindingResult.hasErrors()){
            log.info("but rejected by validation");
            return ResponseEntity.badRequest().body(new ValidationError(bindingResult));
        }

        Bookmark save = bookmarkService.save(user_id, createForm);
        TBookmark tBookmark = TBookmark.builder()
                                .id(save.getId())
                                .name(save.getName())
                                .url(save.getUrl())
                                .directoryId(save.getDirectory().getId())
                                .build();
        return ResponseEntity.ok().body(tBookmark);
    }

    @PostMapping("/bookmarks/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @Validated @RequestBody UpdateForm updateForm,
            BindingResult bindingResult){
        log.info("bookmark update");

        if(bookmarkService.isInvalidBookmarkId(id)){
            bindingResult.addError(
                    new ObjectError(
                            "bookmarkId",
                            "invalid PathVariable bookmark id"));
        }

        if(bindingResult.hasErrors()){
            log.info("but rejected by validation");
            return ResponseEntity.badRequest().body(new ValidationError(bindingResult));
        }

        bookmarkService.update(id, updateForm);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/bookmarks/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("bookmark delete");

        if(bookmarkService.isInvalidBookmarkId(id)){
            log.info("but rejected by invalid PathVariable id");
            return ResponseEntity
                    .badRequest()
                    .body(new ValidationError("invalid PathVariable bookmark id"));
        }

        bookmarkService.delete(id);
        return ResponseEntity.ok().body("ok");
    }
}
