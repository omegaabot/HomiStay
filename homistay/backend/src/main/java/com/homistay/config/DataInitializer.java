package com.homistay.config;

import com.homistay.entity.User;
import com.homistay.enums.Role;
import com.homistay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("support@homistay.com").isEmpty()) {
            User support = User.builder()
                    .email("support@homistay.com")
                    .passwordHash(passwordEncoder.encode("password123"))
                    .fullName("Support Team")
                    .role(Role.SUPPORT_TEAM)
                    .isActive(true)
                    .build();
            userRepository.save(support);
            log.info("Created default support team user: support@homistay.com / password123");
        }
    }
}
