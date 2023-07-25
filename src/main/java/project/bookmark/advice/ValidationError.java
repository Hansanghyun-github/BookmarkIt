package project.bookmark.advice;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ValidationError {
    private List<String> globalErrors = new ArrayList<>();
    private Map<String, String> fieldErrors = new HashMap<>();

    public ValidationError(String str) {
        globalErrors.add(str);
    }

    public ValidationError(BindingResult bindingResult){
        bindingResult.getGlobalErrors().forEach(it -> {
            globalErrors.add(it.getDefaultMessage());
        });

        bindingResult.getFieldErrors().forEach(it -> {
            fieldErrors.put(it.getField(), it.getDefaultMessage());
        });
    }
}
