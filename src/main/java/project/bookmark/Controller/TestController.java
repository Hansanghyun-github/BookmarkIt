package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookmark.Domain.Bookmark;
import project.bookmark.Form.CreateForm;
import project.bookmark.Form.UpdateForm;
import project.bookmark.Service.TestService;

import java.util.List;
import java.util.Optional;

//@Controller
@Slf4j
public class TestController {

    TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public String testList(Model model){
        List<Bookmark> all = testService.findAll();
        model.addAttribute("bookmarks", all);
        return "test/listForm";
    }

    @GetMapping("/test/create")
    public String createForm(@ModelAttribute CreateForm createForm){ return "test/createForm"; }

    @PostMapping("/test/create")
    public String create(@Validated @ModelAttribute CreateForm createForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "test/createForm";
        }

        testService.save(createForm);
        return "redirect:/test";
    }

    @GetMapping("/test/{id}/update")
    public String updateForm(@PathVariable Long id, Model model){
        UpdateForm updateForm = new UpdateForm();
        Optional<Bookmark> byId = testService.findById(id);
        updateForm.setUrl(byId.get().getUrl());
        updateForm.setName(byId.get().getName());

        model.addAttribute("updateForm", updateForm);

        return "test/updateForm";
    }

    @PostMapping("/test/{id}/update")
    public String update(@PathVariable Long id, @Validated @ModelAttribute UpdateForm updateForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "test/updateForm";
        }

        testService.update(id, updateForm);
        return "redirect:/test";
    }

    @PostMapping("/test/{id}/delete")
    public String delete(@PathVariable Long id){
        testService.delete(id);
        return "redirect:/test";
    }
}
