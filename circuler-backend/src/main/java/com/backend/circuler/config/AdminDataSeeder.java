package com.backend.circuler.config;

import com.backend.circuler.entity.Role;
import com.backend.circuler.entity.User;
import com.backend.circuler.enums.UserStatus;
import com.backend.circuler.repository.RoleRepository;
import com.backend.circuler.repository.UserRepository;
import com.backend.circuler.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminDataSeeder.class);

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
            log.info("Administrador do sistema já existe — seeding ignorado.");
            return;
        }

        Role systemAdminRole = roleRepository.findByName(Roles.SYSTEM_ADMIN)
                .orElseThrow(() -> {
                    log.error("Role {} ausente no banco — o script de inicialização não foi executado.",
                            Roles.SYSTEM_ADMIN);
                    return new IllegalStateException(
                            "Role " + Roles.SYSTEM_ADMIN + " não encontrada. Verifique se o script de inicialização do banco foi executado.");
                });

        User admin = new User();
        admin.setName("Administrador");
        admin.setEmail(adminEmail);
        admin.setCpf(adminCpf);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setStatus(UserStatus.ATIVO);
        admin.getRoles().add(systemAdminRole);

        User savedAdmin = userRepository.save(admin);
        log.info("Administrador do sistema criado - id={} roles=[{}]", savedAdmin.getId(), Roles.SYSTEM_ADMIN);
    }
}
