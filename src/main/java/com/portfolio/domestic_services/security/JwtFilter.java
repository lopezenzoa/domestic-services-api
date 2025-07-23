package com.portfolio.domestic_services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Validate the Authorization Header of the request
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // go ahead in the filter chain
            return;
        }

        // 2. Validate the JWT
        // A jwt seems like this: Bearer eyJhbGciOiJIUzI1NiIsI...
        String jwt = authHeader.split(" ")[1];
        boolean isValid = jwtUtil.isValid(jwt);

        if (!isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Load the user from the UserDetailsService
        String username = jwtUtil.getUsername(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);

        // 4. Load the user to the spring security context
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            user.getUsername(), user.getPassword(), user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
