package project.bookmark.Controller;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ErrorMessage {
    private List<String> globalErrors = new ArrayList<>();
    private Map<String, String> fieldErrors = new HashMap<>();

    public ErrorMessage(String str) {
        globalErrors.add(str);
    }

    public ErrorMessage(BindingResult bindingResult){
        bindingResult.getGlobalErrors().forEach(it -> {
            globalErrors.add(it.getDefaultMessage());
        });

        bindingResult.getFieldErrors().forEach(it -> {
            fieldErrors.put(it.getField(), it.getDefaultMessage());
        });
    }
}
