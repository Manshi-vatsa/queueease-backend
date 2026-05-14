package com.queueease.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

private final JwtFilter jwtFilter;

public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())

        // ✅ IMPORTANT for H2 console
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))

        .authorizeHttpRequests(auth -> auth

            // ✅ Allow auth APIs
            .requestMatchers("/api/auth/**").permitAll()

            // ✅ Allow H2 console
            .requestMatchers("/h2-console/**").permitAll()

            // ✅ Allow error
            .requestMatchers("/error").permitAll()

            // ✅ Allow test endpoint
            .requestMatchers("/test").permitAll()

            // ✅ Swagger/OpenAPI access
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**"
            ).permitAll()

            // ✅ Protected APIs
            .requestMatchers("/api/queue/**").hasRole("USER")

            // ✅ Everything else requires authentication
            .anyRequest().authenticated()
        )

        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

}
