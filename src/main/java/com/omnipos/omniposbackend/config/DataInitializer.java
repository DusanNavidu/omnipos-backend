package com.omnipos.omniposbackend.config;

import com.omnipos.omniposbackend.model.Role;
import com.omnipos.omniposbackend.model.Status;
import com.omnipos.omniposbackend.model.User;
import com.omnipos.omniposbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${admin.fullname}")
    private String defaultAdminFullName;

    @Value("${admin.email}")
    private String defaultAdminEmail;

    @Value("${admin.password}")
    private String defaultAdminPassword;

    @Value("${admin.contactNo}")
    private String defaultAdminContactNumber;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        boolean adminExists = userRepository.findByEmail(defaultAdminEmail).isPresent();

        if (!adminExists) {
            User admin = User.builder()
                    .fullName(defaultAdminFullName)
                    .email(defaultAdminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .contactNo(defaultAdminContactNumber)
                    .role(Role.SUPER_ADMIN)
                    .status(Status.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("Default SUPER_ADMIN user created with email: " + defaultAdminEmail);
        } else {
            System.out.println("Default SUPER_ADMIN already exists in database.");
        }
    }
}