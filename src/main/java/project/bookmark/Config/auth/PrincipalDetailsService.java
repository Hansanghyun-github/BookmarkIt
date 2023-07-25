package project.bookmark.Config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.bookmark.Domain.User;
import project.bookmark.Repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);

		if(user.isPresent() == false){
			log.debug("don't found user using username");
			throw new UsernameNotFoundException("username을 찾을 수 없습니다");
		}
		log.debug("found user");

		// session.setAttribute("loginUser", user);
		return new PrincipalDetails(user.get());
	}
}
