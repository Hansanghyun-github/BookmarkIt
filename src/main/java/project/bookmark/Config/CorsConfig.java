package project.bookmark.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
   /*@Override
   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOrigins(
                      "http://localhost:3000",
                      "http://localhost:8080",
                      "http://localhost:8081"
                      ) // 허용할 출처
              .allowedMethods("*") // 허용할 HTTP method
              .allowedHeaders("*")
              .allowCredentials(true) // 쿠키 인증 요청 허용
              .allowedOriginPatterns("*")
              .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
   }*/

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      config.addAllowedOrigin("http://localhost:3000");
      config.addAllowedOriginPattern("*");


      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
   }

}
