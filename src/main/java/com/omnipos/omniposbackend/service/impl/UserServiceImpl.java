package com.omnipos.omniposbackend.service.impl;

import com.omnipos.omniposbackend.dto.AuthDTO;
import com.omnipos.omniposbackend.dto.AuthResponseDTO;
import com.omnipos.omniposbackend.dto.RegisterDTO;
import com.omnipos.omniposbackend.dto.UserDTO;
import com.omnipos.omniposbackend.model.Role;
import com.omnipos.omniposbackend.model.User;
import com.omnipos.omniposbackend.repository.UserRepository;
import com.omnipos.omniposbackend.service.UserService;
import com.omnipos.omniposbackend.util.EmailService;
import com.omnipos.omniposbackend.util.JwtAuthFilter;
import com.omnipos.omniposbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Override
    public UserDTO registerShopAdmin(RegisterDTO registerDTO) {
        log.info("Attempting to register new Shop Admin with email: {}", registerDTO.getEmail());
        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            log.info("Registration failed: Email {} already exists", registerDTO.getEmail());
            throw new RuntimeException("Email already exists...");
        }

        User user = modelMapper.map(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("Successfully registered Shop Admin with ID: {}", savedUser.getId());
        return modelMapper.map(savedUser, UserDTO.class);
    }

//    @Override
//    public UserDTO registerManagerOrCashier(RegisterDTO registerDTO) {
//        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already exists...");
//        }
//
//        User user = modelMapper.map(registerDTO, User.class);
//        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());
//
//        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDTO.class);
//    }

    @Override
    public AuthResponseDTO loginUser(AuthDTO authDTO) {
        log.info("Request to authenticate user with email: {}", authDTO.getEmail());
        System.out.println("Authenticating user: " + authDTO.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword())
            );

            User user = userRepository.findByEmail(authDTO.getEmail())
                    .orElseThrow(() -> {
                        log.warn("Authentication failed: User with email {} not found", authDTO.getEmail());
                        return new UsernameNotFoundException("User not found");
                    });

            log.info("Email send notification process started for user: {}", authDTO.getEmail());
            handleLoginEmailNotification(user);

            String token = jwtUtil.generateToken(user.getEmail());
            log.info("Authentication successful for user: {}. Generated JWT token.", authDTO.getEmail());
            return new AuthResponseDTO(token, user.getRole().name(), user.getId());

        } catch (BadCredentialsException e) {
            log.error("Authentication failed: Invalid credentials for email {}", authDTO.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            log.error("Authentication failed for email {}: {}", authDTO.getEmail(), e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    private void handleLoginEmailNotification(User user) {
        log.info("Handling login email notification for user: {} with role: {}", user.getEmail(), user.getRole());

        try {
            switch (user.getRole()) {
                case SUPER_ADMIN:
                case SHOP_ADMIN:
                    emailService.sendHtmlLoginAlert(
                            user.getEmail(),
                            "Security Alert: New Login Detected",
                            user.getFullName(),
                            user.getRole().name(),
                            "You have successfully logged into the OmniPOS system. This is a security confirmation of your recent activity."
                    );
                    log.info("Admin login alert email dispatched to: {}", user.getEmail());
                    break;

                case MANAGER:
                case CASHIER:
                    if (user.getShopId() != null) {
                        userRepository.findByShopIdAndRole(user.getShopId(), Role.SHOP_ADMIN)
                                .ifPresent(shopAdmin -> {
                                    emailService.sendHtmlLoginAlert(
                                            shopAdmin.getEmail(),
                                            "Staff Login Notification",
                                            shopAdmin.getFullName(),
                                            user.getRole().name(),
                                            "Your staff member <strong>" + user.getFullName() + "</strong> has just logged into the system. Please monitor the store activity."
                                    );
                                    log.info("Staff login notification sent to Shop Admin: {}", shopAdmin.getEmail());
                                });
                    }
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to send login notification email for user {}: {}", user.getEmail(), e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users from the database");
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading UserDetails for Spring Security by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Spring Security: User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}