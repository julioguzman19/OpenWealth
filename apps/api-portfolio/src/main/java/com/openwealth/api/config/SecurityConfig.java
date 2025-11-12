package com.openwealth.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Telling Spring Security don't require authentication for any request
// Can adjust to specific endpoints if needed
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())                 // disable CSRF for now in dev
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()                   // allow ALL endpoints without auth (dev only)
                );

        return http.build();
    }
}
