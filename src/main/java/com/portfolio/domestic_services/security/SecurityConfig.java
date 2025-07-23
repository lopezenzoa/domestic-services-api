package com.portfolio.domestic_services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Users endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Calls endpoints
                        .requestMatchers(HttpMethod.POST, "/api/calls/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET,"/api/calls/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/calls/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/calls/**").hasRole("PROVIDER")

                        // Clients endpoints
                        .requestMatchers("/api/clients/**").hasRole("ADMIN")

                        // Facilities endpoints
                        .requestMatchers(HttpMethod.GET, "/api/facilities/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/facilities/**").hasRole("ADMIN")

                        // Flags endpoints
                        .requestMatchers(HttpMethod.GET, "/api/flags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/flags/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/flags/**").hasAnyRole("ADMIN", "CLIENT")

                        // Providers endpoints
                        .requestMatchers(HttpMethod.GET,"/api/providers/**").authenticated()
                        .requestMatchers("/api/providers/**").hasRole("ADMIN")

                        // Reviews endpoints
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("ADMIN", "CLIENT")

                        // Shifts endpoints
                        .requestMatchers(HttpMethod.POST, "/api/shifts/**").hasAnyRole("PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/shifts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/shifts/**").hasAnyRole("PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/shifts/**").hasAnyRole("ADMIN", "PROVIDER")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
