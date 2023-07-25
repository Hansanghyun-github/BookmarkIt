package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.Directory;
import project.bookmark.Form.DirectoryForm;
import project.bookmark.Form.ToRest.TDirectory;
import project.bookmark.Service.DirectoryService;
import project.bookmark.advice.ValidationError;

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

    @PostMapping("/directories")
    public ResponseEntity<Object> create(
            Authentication authentication,
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
            return ResponseEntity.badRequest().body(new ValidationError(bindingResult));
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long user_id = principalDetails.getUser().getId();

        Directory save = directoryService.save(user_id, directoryForm);
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
            return ResponseEntity.badRequest().body(new ValidationError("invalid PathVariable directory id"));
        }

        directoryService.delete(id);
        return ResponseEntity.ok().body("ok");
    }
}
