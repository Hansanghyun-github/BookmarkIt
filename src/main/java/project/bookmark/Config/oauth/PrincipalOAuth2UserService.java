package project.bookmark.Config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.bookmark.Config.auth.PrincipalDetails;
import project.bookmark.Domain.User;
import project.bookmark.Form.UserForm;
import project.bookmark.Service.UserService;


@Service
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    final private UserService userService;
    @Autowired
    public PrincipalOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    // OAuth2 인증 성공했을때 실행되는 함수
    @Override // TODO 구글 검증, 다른 검증 인터페이스 추가해서 제대로 정리하기
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 login");

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub"); // 1265~
        String username = provider + "_" + providerId; // google_12654894~


        User save;

        if(userService.isDuplicate(username) == false){ // 강제 회원가입
            log.info("OAuth2 user forced join");
            String email = oAuth2User.getAttribute("email");

            UserForm userForm = UserForm.builder()
                    .username(username)
                    .email(email)
                    .password("OAuth2_anythingWord")
                    .build();

            save = userService.save(userForm);
        }
        else // TODO 너무 비효율적인데 - duplicate, find 2번임
            save = userService.findByUsername(username).get();

        return new PrincipalDetails(save, oAuth2User.getAttributes());*/
        return null;
    }

}
