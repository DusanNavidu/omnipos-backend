package com.omnipos.omniposbackend.controller;

import com.omnipos.omniposbackend.dto.AuthDTO;
import com.omnipos.omniposbackend.dto.AuthResponseDTO;
import com.omnipos.omniposbackend.dto.RegisterDTO;
import com.omnipos.omniposbackend.dto.UserDTO;
import com.omnipos.omniposbackend.service.UserService;
import com.omnipos.omniposbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/register-shop-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<APIResponse<UserDTO>> register_shop_admin(@RequestBody RegisterDTO registerDTO) {
        try {
            UserDTO savedUser = userService.registerShopAdmin(registerDTO);
            return ResponseEntity.ok(new APIResponse<>(200, "Shop Admin Registered Successfully", savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new APIResponse<>(400, e.getMessage(), null));
        }
    }

//    @PostMapping("/register-shop-admin")
//    @PreAuthorize("hasRole('SHOP_ADMIN')")
//    public ResponseEntity<APIResponse<UserDTO>> register_manager_or_cashier(@RequestBody RegisterDTO registerDTO) {
//        try {
//            UserDTO savedUser = userService.registerManagerOrCashier(registerDTO);
//            return ResponseEntity.ok(new APIResponse<>(200, "Shop Admin Registered Successfully", savedUser));
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(new APIResponse<>(400, e.getMessage(), null));
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDTO>> login(@RequestBody AuthDTO authDTO) {
        try {
            System.out.println("authDTO = " + authDTO);
            AuthResponseDTO response = userService.loginUser(authDTO);
            return ResponseEntity.ok(new APIResponse<>(200, "Login Successful", response));

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.status(401).body(new APIResponse<>(401, "Invalid Credentials", null));

        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(new APIResponse<>(403, e.getMessage(), null));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Internal Server Error", null));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<APIResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(new APIResponse<>(200, "Success", userService.getAllUsers()));
    }
}