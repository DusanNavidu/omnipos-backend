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
    public UserDTO registerUser(RegisterDTO registerDTO) {
        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists...");
        }

        User user = modelMapper.map(registerDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public AuthResponseDTO loginUser(AuthDTO authDTO) {
        System.out.println("Authenticating user: " + authDTO.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword())
            );

            User user = userRepository.findByEmail(authDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            handleLoginEmailNotification(user);

            String token = jwtUtil.generateToken(user.getEmail());
            return new AuthResponseDTO(token, user.getRole().name(), user.getId());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    private void handleLoginEmailNotification(User user) {
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
                            });
                }
                break;
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}