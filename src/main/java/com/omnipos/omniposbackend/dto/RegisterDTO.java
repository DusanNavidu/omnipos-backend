package com.omnipos.omniposbackend.dto;

import com.omnipos.omniposbackend.model.Role;
import com.omnipos.omniposbackend.model.Status;
import lombok.Data;

@Data
public class RegisterDTO {
    private String shopId;
    private String fullName;
    private String email;
    private String password;
    private String contactNo;
    private Role role;
    private Status status;
}