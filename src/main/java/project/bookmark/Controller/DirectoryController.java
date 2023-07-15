package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.bookmark.Domain.Directory;
import project.bookmark.Form.DirectoryForm;
import project.bookmark.Form.ToRest.TDirectory;
import project.bookmark.Service.DirectoryService;

import javax.annotation.PostConstruct;

@RestController
@CrossOrigin
@Slf4j
public class DirectoryController {
    final private DirectoryService directoryService;
    @Autowired
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @PostConstruct
    public void alreadyMake() {
        DirectoryForm directoryForm = new DirectoryForm();
        directoryForm.setName("root");

        Directory save1 = directoryService.save(directoryForm);
        directoryForm.setPrevDirectoryId(save1.getId());
        directoryService.update(save1.getId(), directoryForm);

        DirectoryForm directoryForm1 = new DirectoryForm();
        directoryForm1.setName("sub folder");

        Directory save2 = directoryService.save(directoryForm1);
        directoryForm1.setPrevDirectoryId(save2.getId());
        directoryService.update(save2.getId(), directoryForm1);
    }

    @PostMapping("/directories")
    public ResponseEntity<Object> create(
            /*@AuthenticationPrincipal PrincipalDetails principal,*/
            @Validated @RequestBody  DirectoryForm directoryForm,
            BindingResult bindingResult){
        log.info("directory create");

        if(directoryService.isInvalidDirectoryId(directoryForm.getPrevDirectoryId())){
            bindingResult.addError(
                    new FieldError(
                            "DirectoryForm",
                            "prevDirectoryId",
                            "Invalid previous Directory Id"));
        }

        if(bindingResult.hasErrors()){
            log.info("but rejected by validation");
            return ResponseEntity.badRequest().body(new ErrorMessage(bindingResult));
        }

        /*Long user_id = principal.getUser().getId();*/
        Directory save = directoryService.save(directoryForm);
        TDirectory tDirectory = TDirectory.builder()
                                    .id(save.getId())
                                    .name(save.getName())
                                    .prevDirectoryId(save.getPrevDirectoryId())
                                    .build();
        return ResponseEntity.ok().body(tDirectory);
    }

    @DeleteMapping("/directories/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if(directoryService.isInvalidDirectoryId(id)){
            return ResponseEntity.badRequest().body(new ErrorMessage("invalid PathVariable directory id"));
        }

        directoryService.delete(id);
        return ResponseEntity.ok().body("ok");
    }
}
