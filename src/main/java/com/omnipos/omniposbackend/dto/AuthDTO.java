package com.omnipos.omniposbackend.dto;

import lombok.Data;

/**
 * @author Dusan
 * @date 7/21/2025
 */

@Data
public class AuthDTO {
    private String email;
    private String password;
}