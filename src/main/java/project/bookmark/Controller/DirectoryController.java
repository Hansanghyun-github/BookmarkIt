package project.bookmark.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Form.DirectoryForm;
import project.bookmark.Service.DirectoryService;

@Controller
public class DirectoryController {
    final private DirectoryService directoryService;
    @Autowired
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/directories/create")
    public String createForm(@ModelAttribute DirectoryForm directoryForm) {
        return "directories/createForm";
    }

    @PostMapping("/directories/create")
    public String create(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Validated @ModelAttribute DirectoryForm directoryForm,
            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "directories/createForm";
        }

        Long user_id = principal.getUser().getId();
        directoryService.save(user_id, directoryForm);
        return "redirect:/bookmarks";
    }
}
