package org.spring.liquibase.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final CustomUserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain defolterSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
                .requestMatchers(HttpMethod.GET, "/books/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
                .anyRequest().permitAll()

        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);

        http.httpBasic();


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider defaultAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;

    }

}
