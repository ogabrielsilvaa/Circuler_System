package com.backend.circuler.config;

import com.backend.circuler.entity.Role;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.repository.RoleRepository;
import com.backend.circuler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminDataSeeder implements ApplicationRunner {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.cpf}")
    private String adminCpf;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataSeeder(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "Role ROLE_ADMIN não encontrada. Verifique se o script de inicialização do banco foi executado."));

        Role rootAdminRole = roleRepository.findByName("ROLE_ROOT_ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "Role ROLE_ROOT_ADMIN não encontrada. Verifique se o script de inicialização do banco foi executado."));

        User admin = new User();
        admin.setName("Administrador");
        admin.setEmail(adminEmail);
        admin.setCpf(adminCpf);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setStatus(UserStatus.ATIVO);
        admin.getRoles().add(adminRole);
        admin.getRoles().add(rootAdminRole);

        userRepository.save(admin);
    }
}
