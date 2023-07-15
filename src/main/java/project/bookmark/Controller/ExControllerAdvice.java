package project.bookmark.Controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> httpMsgNotReadableEx(
            HttpMessageNotReadableException e){
        log.info("HttpMessageNotReadableException");
        return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorMessage> typeMismatchEx(
            TypeMismatchException e){
        log.info("TypeMismatchException");
        return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> methodArgumentTypeMismatchEx(
            MethodArgumentTypeMismatchException e){
        log.info("MethodArgumentTypeMismatchEx");
        return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
    }
}
