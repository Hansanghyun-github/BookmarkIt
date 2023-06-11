package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Domain.Directory;
import project.bookmark.Domain.User;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Repository.BookmarkSearchCond;
import project.bookmark.Service.BookmarkService;
import project.bookmark.Service.DirectoryService;

import java.util.List;
import java.util.Optional;

@Controller
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
    public String bookmarks(
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model) {
        Long user_id = principal.getUser().getId();
        List<Bookmark> bookmarks = bookmarkService.findAll(user_id);
        List<Directory> directories = directoryService.findByUserId(user_id);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("directories", directories);
        return "bookmarks/listForm";
    }

    @GetMapping("/bookmarks/create")
    public String createForm(@ModelAttribute CreateForm createForm){ return "bookmarks/createForm"; }

    @PostMapping("/bookmarks/create")
    public String create(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Validated @ModelAttribute CreateForm createForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "bookmarks/createForm";
        }

        Long user_id = principal.getUser().getId();

        bookmarkService.save(user_id, createForm);

        return "redirect:/bookmarks";
    }

    @GetMapping("/bookmarks/{id}/update")
    public String updateForm(@PathVariable Long id, Model model){
        UpdateForm updateForm = new UpdateForm();
        Optional<Bookmark> byId = bookmarkService.findById(id);
        updateForm.setSiteUrl(byId.get().getSiteUrl());
        updateForm.setExplanation(byId.get().getExplanation());

        model.addAttribute("updateForm", updateForm);

        return "bookmarks/updateForm";
    }

    @PostMapping("/bookmarks/{id}/update")
    public String update(@PathVariable Long id, @Validated @ModelAttribute UpdateForm updateForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "bookmarks/updateForm";
        }

        bookmarkService.update(id, updateForm);
        return "redirect:/bookmarks";
    }

    @PostMapping("/bookmarks/{id}/delete")
    public String delete(@PathVariable Long id){
        bookmarkService.delete(id);
        return "redirect:/bookmarks";
    }
}
