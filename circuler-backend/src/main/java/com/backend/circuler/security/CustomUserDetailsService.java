package com.backend.circuler.security;

import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = loadUserEntityByUsername(email);

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public User loadUserEntityByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndStatusNot(email, UserStatus.APAGADO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}