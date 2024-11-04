package com.todoTask.taskLog;

import com.todoTask.taskLog.service.PasswordService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.todoTask.taskLog.entity.roleEnum;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
@Profile({"deploy", "development"})
public class SessionSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/TaskAPI/v1/login/",
            "/TaskAPI/v1/logout/",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

       private static final String[] ADMIN_AUTH = {
            "/TaskAPI/v1/UserAccount/newUserAcc/",
            "/TaskAPI/v1/UserAccount/userNameFindAcc/**"
    };


    @Bean
    public SecurityFilterChain apiAdminFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(ADMIN_AUTH).hasAuthority(roleEnum.ADMIN.name())
                        .requestMatchers(AUTH_WHITELIST).permitAll()  // Publicly accessible
                        .anyRequest().authenticated()

                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Session creation policy
                )
                .csrf(csrf -> csrf.disable());  // Disable CSRF for REST APIs

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new PasswordService();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));  // Frontend URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));  // Allowed HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("*"));  // Allow all headers
        configuration.setAllowCredentials(true);  // Allow credentials (for session cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Apply CORS configuration to all endpoints
        return source;
    }
}
