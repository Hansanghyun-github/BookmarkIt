package project.bookmark.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import project.bookmark.Controller.AuthController;
import project.bookmark.exception.InValidRefreshException;

import java.util.Date;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class TokenControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request){
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(InValidRefreshException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInValidRefreshException(InValidRefreshException ex, WebRequest request){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
