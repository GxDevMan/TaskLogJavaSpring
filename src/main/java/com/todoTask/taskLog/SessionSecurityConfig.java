package com.todoTask.taskLog;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Profile({"deploy", "development"})
public class SessionSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/TaskAPI/v1/login/",
            "/TaskAPI/v1/logout/",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] ADMIN_AUTH = {
            "/TaskAPI/v1/UserAccount/**",
            "/TaskAPI/v1/Task/**"
    };

    private static final String[] USER_AUTH = {
            "/TaskAPI/v1/UserAccount/updateUserAcc/",
            "/TaskAPI/v1/Task/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(AUTH_WHITELIST).permitAll() // Allow login and register without authentication
                                .requestMatchers(ADMIN_AUTH).hasAuthority("ADMIN")
                                .requestMatchers(USER_AUTH).hasAuthority("USER")
                                .anyRequest().authenticated()
                ).
                sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // Replace with your frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all endpoints
        return source;
    }
}
