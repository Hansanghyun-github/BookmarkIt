package project.bookmark.filter;

import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import project.bookmark.advice.ErrorMessage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtExceptionHandlingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (IllegalArgumentException |
                 SignatureVerificationException |
                 TokenExpiredException |
                 MissingClaimException |
                 IncorrectClaimException ex){
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(new ErrorMessage(
                    HttpStatus.FORBIDDEN.value(),
                    new Date(),
                    ex.getMessage(),
                    ""));

            response.getWriter().write(result);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
