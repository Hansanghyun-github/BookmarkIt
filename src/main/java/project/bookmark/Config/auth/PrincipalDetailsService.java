package project.bookmark.Config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import project.bookmark.Domain.User;
import project.bookmark.Repository.UserRepository;

import javax.naming.Binding;
import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override // 로그인 인증할때 자동으로 실행되는 method, not OAuth2
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("not matching user");
        }else {
            return new PrincipalDetails(user.get());
        }
    }
}
