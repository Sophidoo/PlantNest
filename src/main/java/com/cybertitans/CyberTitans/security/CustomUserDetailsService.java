package com.cybertitans.CyberTitans.security;

import com.cybertitans.CyberTitans.enums.Roles;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No User found with: " + usernameOrEmail));


        Roles userRole = user.getRole();
        SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(userRole.toString());



        return new org.springframework.security.core.userdetails.User(user.getEmail()
                ,user.getPassword(), Collections.singleton(authorities));
    }
}
