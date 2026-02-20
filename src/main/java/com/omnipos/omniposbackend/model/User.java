package com.omnipos.omniposbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author Dusan
 * @date 2/19/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@Builder
public class User {
    @Id
    private String id;
    private String shopId;
    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String contactNo;
    private Role role; // SHOP_ADMIN, SHOP_ADMIN, MANAGER, CASHIER
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}