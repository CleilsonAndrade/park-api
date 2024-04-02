package br.com.cleilsonandrade.parkapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.cleilsonandrade.parkapi.jwt.JwtAuthenticationEntryPoint;
import br.com.cleilsonandrade.parkapi.jwt.JwtAuthorizationFilter;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

  private static final String[] SWAGGER_LIST = {
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resource/**",
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .formLogin(formLogin -> formLogin.disable())
        .httpBasic(basic -> basic.disable())
        .authorizeHttpRequests(
            auth -> auth
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(SWAGGER_LIST)
                .permitAll()
                .anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
        .build();
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
