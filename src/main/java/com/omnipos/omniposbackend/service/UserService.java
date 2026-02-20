package com.omnipos.omniposbackend.service;

import com.omnipos.omniposbackend.dto.AuthDTO;
import com.omnipos.omniposbackend.dto.AuthResponseDTO;
import com.omnipos.omniposbackend.dto.RegisterDTO;
import com.omnipos.omniposbackend.dto.UserDTO;

import java.util.List;

/**
 * @author Dusan
 * @date 2/19/2026
 */

public interface UserService {
    UserDTO registerShopAdmin(RegisterDTO registerDTO);
//    UserDTO registerManagerOrCashier(RegisterDTO registerDTO);
    AuthResponseDTO loginUser(AuthDTO authDTO);    // Login
    List<UserDTO> getAllUsers();
}