package project.bookmark.Config.auth;

import com.nimbusds.openid.connect.sdk.assurance.evidences.ElectronicSignatureEvidence;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.bookmark.Domain.Role;
import project.bookmark.Domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// Authentication 객체에 저장할 수 있는 유일한 타입
// UserDetails 와 OAuth2USer 를 동시에 implements 하여
// 양쪽 멤버를 이 클래스 하나로 받을 수 있음
public class PrincipalDetails implements UserDetails, OAuth2User {
    private static final long serialVersionUID = 1L;
    private User user;
    private Map<String, Object> attributes;

    // 일반 시큐리티 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2.0 로그인시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
        collect.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                if(user.getRole() == Role.ROLE_USER)
                    return "ROLE_USER";
                else return "ROLE_ADMIN";
            }
        });
        return collect;
    }

    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 PrimaryKey
    @Override
    public String getName() {
        return user.getId()+"";
    }
}
