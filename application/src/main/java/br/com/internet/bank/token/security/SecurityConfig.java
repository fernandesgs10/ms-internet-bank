package br.com.internet.bank.token.security;

import br.com.internet.bank.token.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings({"unused", "removal"})
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/v1/internet-bank/auth/login"),
                                AntPathRequestMatcher.antMatcher("/h2/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui/*"),
                                AntPathRequestMatcher.antMatcher("/webjars/**"),
                                AntPathRequestMatcher.antMatcher("/v3/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST),
                                AntPathRequestMatcher.antMatcher(HttpMethod.PUT),
                                AntPathRequestMatcher.antMatcher(HttpMethod.DELETE))
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/v1/internet-bank/auth/login"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/swagger-resources/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/*"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/v3/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE))
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
