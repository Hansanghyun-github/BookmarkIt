package project.bookmark.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Form.JoinForm;
import project.bookmark.Form.LoginForm;
import project.bookmark.Form.UserForm;
import project.bookmark.Repository.UserRepository;
import project.bookmark.Service.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Controller
@Slf4j
public class UserController {
    /**
     * 일반 join 후 로그인 왜안되는지, 패스워드 체크하기 - PrincipalDetailsService 에 @Service 안붙혔음
     * TODO 로그인 된 상태로 loginForm 갔을때 로그인 또 못하게
     */

    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private UserService userService;

    @Autowired
    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainForm(){ return "mainForm"; }

    @GetMapping("/joinForm")
    public String joinForm(@ModelAttribute JoinForm joinDto){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult){
        // TODO 비밀번호 validation 따로 구현
        String validPassword = joinForm.getPassword();
        if(validPassword.length() >= 8) {
            boolean flagNum = false, flagChar = false;
        }

        // 회원 중복 체크
        if(userService.isDuplicate(joinForm.getUsername())){
            //bindingResult.reject("duplicateUser", null, null);
            bindingResult.rejectValue("username", "duplicateUser", null, null);
        }
        
        // 비밀번호 같은지 체크
        if(joinForm.getPassword().equals(joinForm.getPasswordCheck()) == false){
            bindingResult.rejectValue("passwordCheck", "NotEqualPassword", null, null);
        }

        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "joinForm";
        }

        String password = bCryptPasswordEncoder.encode(joinForm.getPassword());
        UserForm userForm = UserForm.builder()
                .username(joinForm.getUsername())
                .email(joinForm.getEmail())
                .password(password)
                .build();

        userService.save(userForm);

        return "redirect:/loginForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(
            @ModelAttribute LoginForm loginForm,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String exception,
            Model model
    ){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "loginForm";
    }

    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principal){
        System.out.println("--------------------------- user ---------------------------");
        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }
        return "user's page";
    }
}
