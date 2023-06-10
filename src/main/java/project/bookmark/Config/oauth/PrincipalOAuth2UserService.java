package project.bookmark.Config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;
import project.bookmark.Repository.UserRepository;
import project.bookmark.Service.DirectoryService;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DirectoryService directoryService;

    // OAuth2 인증 성공했을때 실행되는 함수
    @Override // TODO 구글 검증, 다른 검증 인터페이스 추가해서 제대로 정리하기
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("OAuth2 로그인");

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub"); // 1265~
        String username = provider + "_" + providerId; // google_12654894~

        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){ // 강제 회원가입
            System.out.println("강제 회원가입 진행");
            String email = oAuth2User.getAttribute("email");

            user = Optional.of(
                    User.builder()
                    .username(username)
                    .password("OAuth2_anythingWord") // TODO 랜덤하게 바꿔주기
                    .email(email)
                    .role(Role.ROLE_USER)
                    .provider(provider)
                    .providerId(providerId)
                    .bookmarks(new ArrayList<>())
                    .directories(new ArrayList<>())
                    .build());

            User save = userRepository.save(user.get());
            directoryService.createRootDirectory(save.getId());
        }

        return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
    }

}
