package project.bookmark.advice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import project.bookmark.Controller.BookmarkController;
import project.bookmark.Controller.DirectoryController;

@RestControllerAdvice(assignableTypes = {BookmarkController.class, DirectoryController.class})
@Slf4j
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationError> httpMsgNotReadableEx(
            HttpMessageNotReadableException e){
        log.info("HttpMessageNotReadableException");
        return ResponseEntity.badRequest().body(new ValidationError(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ValidationError> typeMismatchEx(
            TypeMismatchException e){
        log.info("TypeMismatchException");
        return ResponseEntity.badRequest().body(new ValidationError(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationError> methodArgumentTypeMismatchEx(
            MethodArgumentTypeMismatchException e){
        log.info("MethodArgumentTypeMismatchEx");
        return ResponseEntity.badRequest().body(new ValidationError(e.getMessage()));
    }
}
