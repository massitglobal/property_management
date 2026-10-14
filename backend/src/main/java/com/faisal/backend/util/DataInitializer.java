package com.faisal.backend.util;

import java.util.Set;

import com.faisal.backend.model.UserInformation;
import com.faisal.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final String adminPassword;
    private final String userPassword;

    public DataInitializer(UserRepository repo,
                           PasswordEncoder encoder,
                           @Value("${app.seed.admin-password:}") String adminPassword,
                           @Value("${app.seed.user-password:}") String userPassword) {
        this.repo = repo;
        this.encoder = encoder;
        this.adminPassword = adminPassword;
        this.userPassword = userPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createIfMissing("admin", adminPassword, Set.of("ADMIN", "USER"));
        createIfMissing("user", userPassword, Set.of("USER"));
    }

    private void createIfMissing(String username, String rawPassword, Set<String> roles) {
        if (repo.findByUsername(username).isPresent()) {
            return;
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            log.warn("Skipping seed user '{}': no password configured", username);
            return;
        }
        UserInformation u = new UserInformation();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setEnabled(true);
        u.getRoles().addAll(roles);
        repo.save(u);
        log.info("Seeded user '{}' with roles {}", username, roles);
    }
}