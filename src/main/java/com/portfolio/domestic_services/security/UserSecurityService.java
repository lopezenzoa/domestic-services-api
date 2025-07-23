package com.portfolio.domestic_services.security;

import com.portfolio.domestic_services.model.User;
import com.portfolio.domestic_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {
    @Autowired private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userEntity = repository.findByUsername(username);

        String[] authorities = { userEntity.get().getRole().toString() };

        return userEntity.map(user -> org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(this.grantedAuthorities(authorities))
                .accountLocked(false)
                .disabled(false)
                .build()).orElse(null);
    }

    private List<GrantedAuthority> grantedAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (String role: roles)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return authorities;
    }
}
