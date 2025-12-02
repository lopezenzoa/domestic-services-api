package com.portfolio.domestic_services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        // ============================
                        //  PUBLIC ENDPOINTS
                        // ============================
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // ============================
                        //  USERS
                        // ============================
                        .requestMatchers("/api/users/me")
                        .hasAnyRole("ADMIN", "PROVIDER", "CLIENT")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // ============================
                        //  💬 CHATS (CLIENT/PROVIDER)
                        // ============================
                        .requestMatchers(HttpMethod.GET, "/api/calls/chats")
                        .hasAnyRole("CLIENT", "PROVIDER", "ADMIN")
                        // ============================
                        //  📞 CALLS
                        // ============================
                        .requestMatchers("/api/calls/me")
                        .hasAnyRole("CLIENT", "PROVIDER")

                        .requestMatchers(HttpMethod.GET, "/api/calls/provider/*/history")
                        .hasRole("PROVIDER")

                        .requestMatchers(HttpMethod.GET, "/api/calls/provider/**")
                        .hasRole("PROVIDER")

                        .requestMatchers(HttpMethod.GET, "/api/calls/client/**")
                        .hasRole("CLIENT")

                        .requestMatchers(HttpMethod.GET, "/api/calls/**")
                        .hasRole("ADMIN")

                        // ============================
                        //  CLIENTS
                        // ============================
                        .requestMatchers("/api/clients/me").hasRole("CLIENT")
                        .requestMatchers("/api/clients/**").hasRole("ADMIN")

                        // ============================
                        //  FACILITIES
                        // ============================
                        .requestMatchers(HttpMethod.GET, "/api/facilities/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/facilities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/facilities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/facilities/**").hasRole("ADMIN")

                        // ============================
                        //  FLAGS
                        // ============================
                        .requestMatchers("/api/flags/me").hasAnyRole("PROVIDER", "CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/flags/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/flags/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/flags/**").hasAnyRole("ADMIN", "CLIENT")

                        // ============================
                        //  SHIFTS
                        // ============================
                        .requestMatchers("/api/providers/shifts/me").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.POST, "/api/providers/shifts/**").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/providers/shifts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/providers/shifts/**").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/providers/shifts/**").hasAnyRole("ADMIN", "PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/providers/*/calls/*/finish").hasRole("PROVIDER")

                        // ============================
                        //  PROVIDERS
                        // ============================
                        .requestMatchers("/api/providers/me").hasRole("PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/providers/**").authenticated()
                        .requestMatchers("/api/providers/**").hasRole("ADMIN")

                        // ============================
                        //  REVIEWS
                        // ============================
                        .requestMatchers("/api/reviews/all").authenticated()
                        .requestMatchers("/api/reviews/me").hasAnyRole("PROVIDER", "CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("ADMIN", "CLIENT")

                        // ============================
                        //  MESSAGES
                        // ============================
                        .requestMatchers("/api/messages/**")
                        .hasAnyRole("CLIENT", "PROVIDER", "ADMIN")

                        // ============================
                        //  RESTO
                        // ============================
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
