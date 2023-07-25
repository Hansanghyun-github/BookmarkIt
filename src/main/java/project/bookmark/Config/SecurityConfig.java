package project.bookmark.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import project.bookmark.Repository.UserRepository;
import project.bookmark.Service.RefreshTokenService;
import project.bookmark.filter.JwtAuthenticationFilter;
import project.bookmark.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    //private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final RefreshTokenService refreshTokenService;
    //private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                /*.and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)        // Authorization fail handler
                .authenticationEntryPoint(unauthorizedHandler)    // Authentication fail handler*/
                /** TODO Access Denied 일때 API 정리 & authentication failed handler, authorization failed handler 정리
                 * 현재 AnonymousAuthenticationFilter에 의해 자동으로 익명사용자가 SecurityContext에 자동으로 등록됨
                 * 이때문에 AccessDeniedExceptionHandling이 원하는대로 되지않음 (AuthenticationEntryPoint로 이동함)
                 * AnonymousAuthenticationFilter 공부 후에 고친다
                 */

                .and()
                .apply(new MyCustomDsl())

                .and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/user/**")
                        .hasAuthority("ROLE_USER")
                        .antMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .antMatchers("/api/auth/**", "/error/**")
                        .permitAll()
                        .antMatchers("/bookmarks/**", "/directories/**")
                        .authenticated()
                        .anyRequest()
                        .denyAll()
                );

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilterAfter(new JwtAuthenticationFilter(authenticationManager, refreshTokenService)
                            ,LogoutFilter.class)
                    .addFilterAfter(new JwtAuthorizationFilter(userRepository)
                            ,LogoutFilter.class);
        }
    }
}
