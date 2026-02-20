package com.omnipos.omniposbackend.dto;

import com.omnipos.omniposbackend.model.Role;
import com.omnipos.omniposbackend.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Dusan
 * @date 2/19/2026
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String shopId;
    private String fullName;
    private String email;
    private String password;
    private String contactNo;
    private Role role;
    private Status status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}