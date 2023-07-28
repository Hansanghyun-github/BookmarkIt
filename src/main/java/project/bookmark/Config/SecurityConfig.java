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
import project.bookmark.Config.auth.JwtAccessDeniedHandler;
import project.bookmark.Config.auth.JwtAuthenticationEntryPoint;
import project.bookmark.Repository.UserRepository;
import project.bookmark.Service.RefreshTokenService;
import project.bookmark.filter.JwtAuthenticationFilter;
import project.bookmark.filter.JwtAuthorizationFilter;
import project.bookmark.filter.JwtExceptionHandlingFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final RefreshTokenService refreshTokenService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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

                .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)        // Authorization fail handler
                .authenticationEntryPoint(unauthorizedHandler)    // Authentication fail handler

                .and()
                .apply(new MyCustomDsl())

                .and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/user/**")
                        .hasAuthority("ROLE_USER")
                        .antMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .antMatchers("/api/auth/**", "/login", "/error/**")
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
                    .addFilterBefore(corsConfig.corsFilter(), LogoutFilter.class)
                    .addFilterAfter(new JwtAuthorizationFilter(userRepository)
                            ,ExceptionTranslationFilter.class)
                    .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, refreshTokenService)
                            , JwtAuthorizationFilter.class)
                    .addFilterBefore(new JwtExceptionHandlingFilter(), JwtAuthorizationFilter.class);
        }
    }
}
