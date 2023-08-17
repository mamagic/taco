package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation
             .authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web
             .builders.HttpSecurity;
import org.springframework.security.config.annotation.web
                        .configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .requestMatchers(HttpMethod.OPTIONS).permitAll() // needed for Angular/CORS
        .requestMatchers("/design", "/orders/**")
            .permitAll()
            //.access("hasRole('ROLE_USER')")
        .requestMatchers(HttpMethod.PATCH, "/ingredients").permitAll()
        .requestMatchers("/**").access("permitAll")
        
      .and()
        .formLogin()
          .loginPage("/login")
          
      .and()
        .httpBasic()
          .realmName("Taco Cloud")
          
      .and()
        .logout()
          .logoutSuccessUrl("/")
          
      .and()
        .csrf()
          .ignoringRequestMatchers("/h2-console/**", "/ingredients/**", "/design", "/orders/**")

      // Allow pages to be loaded in frames from the same origin; needed for H2-Console
      .and()  
        .headers()
          .frameOptions()
            .sameOrigin()
      ;
    return http.build();
  }

  @Bean
  public PasswordEncoder encoder() {
//    return new StandardPasswordEncoder("53cr3t");
    return NoOpPasswordEncoder.getInstance();
  }
  
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
          throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
  }
}
